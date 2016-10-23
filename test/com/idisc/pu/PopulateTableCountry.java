/*
 * Copyright 2016 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idisc.pu;

import com.bc.io.CharFileIO;
import com.bc.jpa.JpaContext;
import com.bc.jpa.dao.BuilderForUpdate;
import com.idisc.pu.entities.Country;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 24, 2016 12:16:24 PM
 */
public class PopulateTableCountry extends TestStub{
    
    public static void main(String [] args) {
        try{
            new PopulateTableCountry().execute();
        }catch(IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public void execute() throws MalformedURLException, IOException, ParseException {
        
//        JSONObject iso2ToName = getJson(new URL("http://country.io/names.json"));
//        JSONObject iso2ToIso3 = getJson(new URL("http://country.io/iso3.json"));

        Path path = Paths.get(System.getProperty("user.home"), "Documents", 
                "NetBeansProjects", "idiscpu", "test", "com", "idisc", "pu", "countries.json"
        );
        JSONObject json = this.getJson(path);
        
        JSONObject countries = (JSONObject)json.get("countries");
        JSONArray list = (JSONArray)countries.get("country");
        
        final JpaContext jpaContext = this.getJpaContext();
        
        try(BuilderForUpdate<Country> dao = jpaContext.getBuilderForUpdate(Country.class)) {

            dao.begin();

            for(Object elem : list) {

                JSONObject countryJson = (JSONObject)elem;

                String codeIso2 = (String)countryJson.get("countryCode");
                String codeIso3 = (String)countryJson.get("isoAlpha3");
                String name = (String)countryJson.get("countryName");
                short numeric = Short.parseShort((String)countryJson.get("isoNumeric"));
System.out.println(codeIso3+'\t'+name+'\t'+numeric+'\t'+codeIso2);            
                Country country = new Country();
                country.setCodeIso2(codeIso2);
                country.setCodeIso3(codeIso3);
                country.setCountry(name);
                country.setCountryid(numeric);
                
                Country found = dao.getEntityManager().find(Country.class, numeric);
                if(found != null) {
                    dao.merge(country);
                }else{
                    dao.persist(country);
                }
            }

            dao.commit();
        }
    }  

    private JSONObject getJson(Path path) throws IOException, ParseException{
        String jsonString = new String(Files.readAllBytes(path));
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject)parser.parse(jsonString);
        return json;
    }
    
    private JSONObject getJson(URL url) throws MalformedURLException, IOException, ParseException{
        InputStream in = url.openStream();
        CharSequence jsonChars = new CharFileIO().readChars(in);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject)parser.parse(jsonChars.toString());
        return json;
    }
}
