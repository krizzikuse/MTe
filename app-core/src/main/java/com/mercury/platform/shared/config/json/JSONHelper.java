package com.mercury.platform.shared.config.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.json.deserializer.AdrComponentJsonAdapter;
import com.mercury.platform.shared.config.json.deserializer.AdrTrackerGroupDeserializer;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import currencydata.POETradeCurrencyData;
import currencydata.CurrencyData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.logging.log4j.Level;

public class JSONHelper {
    private Logger logger = LogManager.getLogger(JSONHelper.class.getSimpleName());
    private String dataSource;

    public JSONHelper(String dataSource) {
        this.dataSource = dataSource;
    }

    public JSONHelper() {
        this.dataSource = dataSource;
    }

    public static List<AdrComponentDescriptor> getJsonAsObject(String jsonStr) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            JsonParser jsonParser = new JsonParser();
            return gson.fromJson(
                    jsonParser.parse(jsonStr),
                    new TypeToken<List<AdrComponentDescriptor>>() {
                    }.getType());
        } catch (IllegalStateException | JsonSyntaxException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing string: " + jsonStr, e));
            return null;
        }
    }

    public <T> List<T> readArrayData(TypeToken<List<T>> typeToken) {
        try {

            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(AdrTrackerGroupDescriptor.class, new AdrTrackerGroupDeserializer())
                    .registerTypeHierarchyAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();

            JsonParser jsonParser = new JsonParser();
            try (JsonReader reader = new JsonReader(new FileReader(dataSource))) {
                return gson.fromJson(
                        jsonParser.parse(reader),
                        typeToken.getType());
            }
        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
            logger.error(e);
            return null;
        }
    }

    public class FooResponse<T> {
//        private String status;
//        private String message;
        private T data;
        // constructor, getters
    }

//    public <T> FooResponse<T> getResponse(/* parameters for retrieval ,*/ final Class<T> dataClass) {
//        //final String rawResponse = getRawResponse(); // ... e.g. via some http client library
//        return gson.fromJson(rawResponse, getType(FooResponse.class, dataClass));
//    }    
//    Type getType(Class<?> rawClass, Class<?> parameter) {
//        
//      return new ParametrizedType() {
//        @Override
//        public Type[] getActualTypeArguments() {
//           return new Type[] {parameter};
//        }
//        @Override
//        public Type getRawType() {
//          return rawClass;
//        }
//        @Override
//        public Type getOwnerType() {
//          return null;
//        }   
//      }
//    }    
    
    public Map<String, String> readPOEDotComCurrencyData() {
        try {
            Type mapType = new TypeToken<Map<String, String> >() {}.getType();
            
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            JsonReader reader = new JsonReader(new FileReader(dataSource));
            //String jsonObj = gson.fromJson (jsonParser.parse(reader), String.c).getAsJsonObject();
            Map<String,String> cd = gson.fromJson(jsonParser.parse(reader), mapType);
            
            return(cd);
            //System.out.println(cd);
            
        } catch (Exception ex) {
            //System.out.println(ex);
            logger.log(Level.ERROR, ex);
            return(null);
        }
        
    }
    //public <T> List<T> readArrayDataVar(TypeToken<List<T>> typeToken) {
    public CurrencyData readPOETradeCurrency() {
       // try {
            
            //Reader reader = new FileReader(dataSource);
        //see https://stackoverflow.com/questions/5796948/how-to-parse-dynamic-json-fields-with-gson            
        
        
        
        try {
            Gson gson = new Gson();
        //    Type mapType = new TypeToken<Map<String, POETradeCurrencyDataEntry> >() {}.getType(); // define generic type  WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur
        //    JsonParser jsonParser = new JsonParser();
        //    JsonReader reader = new JsonReader(new FileReader(dataSource));
        //    Map<String, POETradeCurrencyDataEntry>  as = gson.fromJson(jsonParser.parse(reader), mapType); //WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur
            //Type mapType = new TypeToken<Map<String, Map<String,Integer> > >() {}.getType(); // WORKS! -> define generic type

        //Map<String, POETradeCurrencyDataEntry> result= gson.fromJson(jsonParser.parse(reader), mapType);
        //Map<String, Map<String,Integer> > as = gson.fromJson(jsonParser.parse(reader), mapType);// WORKS!
            
            Type mapType = new TypeToken<Map<String, POETradeCurrencyData> >() {}.getType(); // define generic type  WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur  
            JsonParser jsonParser = new JsonParser();
            JsonReader reader = new JsonReader(new FileReader(dataSource));
            JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();
            jsonObj.get(dataSource);
            Set<Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
            int i=0;
            //Hashtable<String key,T data> aa = new Hashtable();
//            Hashtable<String, CurrencyData> poetcd = new Hashtable();

            CurrencyData cl = new CurrencyData();
            for(Map.Entry<String,JsonElement> entry : entrySet){
                //Map.Entry<String,JsonElement> entry
                //POETradeCurrencyDataEntry2 ab = new POETradeCurrencyDataEntry2(entry);
                cl.addPOETradeCurrency(entry);
//                System.out.println("this is entry #" + i +":" + entry +"\n");
//                if (entry.getValue().isJsonObject()) { 
//                    System.out.println("Key:"+entry.getKey()+"has Object Type:" +entry.getValue());
//
//                } else if (entry.getValue().isJsonPrimitive())
//                    System.out.println("Key:"+entry.getKey()+"has primitive Type:" +entry.getValue());
            }
            //System.out.println("\"Success!!!\"");
            
            return cl;
            
        } catch (Exception ex) {
            //System.out.println(ex);
            logger.log(Level.ERROR, ex);
        }
        return(null);
    }
    
//    public <T> List<T> readArrayDataVar2(TypeToken<List<T>> typeToken) {
//       // try {
//            
//            //Reader reader = new FileReader(dataSource);
//        //see https://stackoverflow.com/questions/5796948/how-to-parse-dynamic-json-fields-with-gson            
//        
//        
//        
//        try {
//            Gson gson = new Gson();
//            //Type mapType = new TypeToken<Map<String, POETradeCurrencyDataEntry> >() {}.getType(); // define generic type  WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur
//            //Type mapType = new TypeToken<Map<String, POETradeCurrencyDataEntry> >() {}.getType(); // define generic type  WORKS AS WELL when "jdaslkasdjl":"sadjkdjsakdasl" doesnt occur
//            //Type mapType = new TypeToken<Map<String, Map<String,T> > >() {}.getType(); // WORKS! -> define generic type
////            Type mapType = new TypeToken<Map<String, POETradeCurrencyDataEntryType > >() {}.getType(); // WORKS! -> define generic type
////            JsonParser jsonParser = new JsonParser();
////            JsonReader reader = new JsonReader(new FileReader(dataSource));
////            //Map<String, POETradeCurrencyDataEntry> result= gson.fromJson(jsonParser.parse(reader), mapType);
////            //Map<String, Map<String,T> > as = gson.fromJson(jsonParser.parse(reader), mapType);// WORKS!
////            Map<String, POETradeCurrencyDataEntry2 > as = gson.fromJson(jsonParser.parse(reader), mapType);// WORKS!
////            mapType = new TypeToken<Map<String,POETradeCurrencyDataEntry2>>(){}.getType();
////            System.out.println("\"Success!!!\"");
//
//    //JsonArray response = your_array_response;
//    JsonReader reader = new JsonReader(new FileReader(dataSource));
//    JsonParser jsonParser = new JsonParser();
//    jsonParser.
//    for (int i = 0; i < response.size(); i++) {
//
//        JsonObject object = response.get(i).getAsJsonObject();
//
//        JsonObject dataObject = object.get("data").getAsJsonObject();
//        JsonElement element = dataObject.get("child");
//
//        if (element.isJsonObject()) {
//            JsonObject childObject = element.getAsJsonObject();
//            System.out.println("JsonObject : Foo = "+childObject.get("foo").getAsString());
//        } else if (element.isJsonPrimitive()) {
//            System.out.println("A String: value = " + element.getAsString());
//        }
//
//    }
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//        return(null);
//    }
    public class T1 <T> {
        T data;
        public T1(T data) {
            this.data = data;
    }
}
    
//    public void JacksonObjectMapperExample() {
//        //read json file data to String
//        try {
//		byte[] jsonData = Files.readAllBytes(Paths.get(dataSource));
//        
////                byte [] jsonData = new JsonReader(new FileReader(dataSource));
////                FileReader a = new FileReader(dataSource);
//		
//		//create ObjectMapper instance
//		ObjectMapper objectMapper = new ObjectMapper();
//		
//		//convert json string to object
//		//Employee emp = objectMapper.readValue(jsonData, Employee.class);
//		CurrencyData emp = objectMapper.readValue(jsonData, CurrencyData.class);
//                
//		System.out.println("Employee Object\n"+emp);
//		
//		//convert Object to json string
//		// emp1 = createEmployee();
//		 POETradeCurrencyDataEntry2 = createEmployee();
//		//configure Object mapper for pretty print
//		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//		
//		//writing to console, can write to any output stream such as file
//		StringWriter stringEmp = new StringWriter();
//		objectMapper.writeValue(stringEmp, emp1);
//		System.out.println("Employee JSON is\n"+stringEmp);
//                
//                } catch (Exception ex) {}
//    }
    
    

    public <T> T readMapData(String key, TypeToken<T> typeToken) {
        try {
            //System.out.println("im now in readMapData");
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            try (JsonReader reader = new JsonReader(new FileReader(dataSource))) {
                return gson.fromJson(
                        jsonParser.parse(reader)
                                .getAsJsonObject()
                                .get(key),
                        typeToken.getType());
            }
        } catch (IOException | IllegalStateException e) {
            logger.error(e);
            return null;
        }
    }

    public void writeMapObject(String key, Map<?, ?> object) {
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

            try (JsonWriter writer = new JsonWriter(new FileWriter(dataSource))) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add(key, gson.toJsonTree(object));
                gson.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public <T> void writeListObject(List<?> object, TypeToken<List<T>> typeToken) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            try (JsonWriter writer = new JsonWriter(new FileWriter(dataSource))) {
                gson.toJson(object, typeToken.getType(), writer);
            }
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public List<AdrComponentDescriptor> getJsonAsObjectFromFile(String filePath) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            JsonParser jsonParser = new JsonParser();
            try (JsonReader reader = new JsonReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)))) {
                return gson.fromJson(
                        jsonParser.parse(reader),
                        new TypeToken<List<AdrComponentDescriptor>>() {
                        }.getType());
            } catch (IOException e) {
                MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing from file:", e));
            }
        } catch (IllegalStateException | JsonSyntaxException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing from file:", e));
            return null;
        }
        return null;
    }
}
