/*
 * Copyright 2017 NUROX Ltd.
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

package com.idisc.pu.functions;

import com.bc.jpa.metadata.JpaMetaData;
import com.bc.sql.SQLUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author Chinomso Bassey Ikwuagwu on Sep 20, 2017 4:40:02 PM
 */
public class GetColumnNamesOfType implements BiFunction<Class, Class, List<String>> {

    private final JpaMetaData metaData;

    public GetColumnNamesOfType(JpaMetaData jpaMetaData) {
        this.metaData = Objects.requireNonNull(jpaMetaData);
    }

    @Override
    public List<String> apply(Class entityClass, Class type) {
        final String [] columns = metaData.getColumnNames(entityClass);
        final int [] types = metaData.getColumnDataTypes(entityClass);
        final List<String> output = new ArrayList<>(columns.length);
        int i = 0;
        for(String column : columns) {
            if(SQLUtils.getClass(types[i++]) == type) {
                output.add(column);
            }
        }
        return Collections.unmodifiableList(output);
    }
}
