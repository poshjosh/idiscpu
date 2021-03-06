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
import com.bc.jpa.context.PersistenceUnitContext;
import com.idisc.pu.entities.Timezone;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimeZone;
import javax.persistence.NoResultException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.bc.jpa.dao.Update;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 3, 2016 3:33:30 PM
 */
public class PopulateTableTimezone extends TestStub{
    
    private final StringBuilder insertQueries = new StringBuilder();
    
    public static void main(String [] args) {
//System.out.println(TimeZone.getTimeZone("EST5EDT"));
        try{
            final PopulateTableTimezone action = new PopulateTableTimezone();
            action.execute(false);
            System.out.println(action.getInsertQueries());
        }catch(IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    public void execute(boolean updateDatabase) throws MalformedURLException, IOException, ParseException {
        
        Path path = Paths.get(System.getProperty("user.home"), "Documents", 
                "NetBeansProjects", "idiscpu", "src", "test", "java", "com", "idisc", "pu", "timezones.json"
        );
        JSONObject json = this.getJson(path);
        
        JSONArray timezones = (JSONArray)json.get("timezones");
        
        final PersistenceUnitContext jpaContext = this.getPersistenceUnitContext();
        
        try(Update<Timezone> update = jpaContext.getDaoForUpdate(Timezone.class)) {

            update.begin();

            short i;
            try{
                final String idColumn = jpaContext.getMetaData().getIdColumnName(Timezone.class);
                List<Timezone> found = jpaContext.getDaoForSelect(Timezone.class).from(Timezone.class).ascOrder(idColumn).getResultsAndClose();
                final int size = found.size();
                if(size > Short.MAX_VALUE) {
                    throw new UnsupportedOperationException();
                }
                i = (short)size;
            }catch(NoResultException e) {
                i = 1;
            }
            
            for(Object elem : timezones) {

                JSONObject timezoneJson = (JSONObject)elem;

                String abbr = (String)timezoneJson.get("abbr");
                List utcList = (List)timezoneJson.get("utc");
System.out.println("Abbreviation: "+abbr+".\tNames: "+utcList);  

                if(utcList == null || utcList.isEmpty()) {
                    
                    this.createAndUpdateDatabase(update, i, abbr, timezoneJson);
                    
                    ++i;
                    
                }else{
                    
                    int updated = 0;
                    
                    for(Object utc : utcList) {

                        final String sval = utc.toString();

                        if(sval.startsWith("Etc/")) {
System.out.println("----------------- Skipping "+sval);                        
                            continue;
                        }

                        this.createAndUpdateDatabase(update, i, abbr, sval);
                        
                        ++updated;

                        ++i;
                    }
                    
                    if(updated == 0) {

                        this.createAndUpdateDatabase(update, i, abbr, timezoneJson);
                        
                        ++i;
                    }
                }
            }

            if(updateDatabase) {
                update.commit();
            }else{
                update.getEntityManager().getTransaction().rollback();
            }
        }
    }  
    
    private void createAndUpdateDatabase(Update update, short id, String abbr, JSONObject timezoneJson) {
        String sval = (String)timezoneJson.get("value");
        TimeZone tz = TimeZone.getTimeZone(sval);
if("GMT".equals(sval) && "GMT".equals(tz.getID())) {
System.out.println("----------------- Skipping value: "+sval); 
}
        this.createAndUpdateDatabase(update, id, abbr, sval);
    }

    private Timezone createAndUpdateDatabase(
            Update dao, short id, String abbr, String sval) {
        Timezone timezone = this.createTimezone(id, abbr, sval);
        Timezone found = dao.getEntityManager().find(Timezone.class, id);
        if(found != null) {
            dao.merge(timezone);
        }else{
            dao.persist(timezone);
        }
        return timezone;
    }
    
    private Timezone createTimezone(short id, String abbr, String sval) {
//insert into `all9janews_newsdb`.timezone VALUES(1, 'DST', 'Dateline Standard Time'); 
        insertQueries.append("\ninsert into `all9janews_newsdb`.timezone VALUES("+id+", '"+abbr+"', '"+sval+"');");
        Timezone timezone = new Timezone();
        timezone.setTimezoneid(id);
        timezone.setAbbreviation(abbr);
        timezone.setTimezonename(sval);
        return timezone;
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

    public StringBuilder getInsertQueries() {
        return insertQueries;
    }
}
