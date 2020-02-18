package cn.edu.bucm.face;

import cn.edu.bucm.exceptions.ThePicHasNotFaceException;
import cn.edu.bucm.face.domain.*;
import cn.edu.bucm.face.enums.GrandType;
import cn.edu.bucm.utils.Contants;
import cn.edu.bucm.utils.UnsafeOkHttpClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 璁块棶saas骞冲彴,閫氳繃鑾峰彇token鎺堟潈鏂瑰紡璁块棶銆傝繖绉嶆柟寮忕渷鐣ョ涓?姝ヨ幏鍙朿ode
 * 鐩存帴鑾峰彇token锛屾嬁鍒皌oken璁块棶瀹堜繚鎶ょ殑璧勬簮
 * token鐨勬湁鏁堟湡涓哄崐涓皬鏃讹紝濡傛灉杩囨湡锛岀敤refresh_token鍦ㄦ璇锋眰/oauth2/token鍗冲彲锛坮efresh_token鐨勬湁鏁堟湡涓?24灏忔椂锛夎鏌ョ湅exchangeToken鏂规硶
 *
 * @author js
 */
public class VisitSAASAuthorize {

    private static OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient ();



    private static String token;

    public static String basic = "Basic " + Base64.getEncoder().encodeToString((Contants.APP_ID + ":" + Contants.SECURE_KEY).getBytes());


    private static synchronized void doSetToken(String token){
        VisitSAASAuthorize.token = token;
    }

    private static synchronized String doGetToken(){
        return token;
    }

    /**
     * @param authorizationBasic 鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
     * @return Map
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings("ConstantConditions")
    public static String getToken(String authorizationBasic) throws Exception {
//        String token = doGetToken();
//        if (Objects.nonNull(token)){
//            return token;
//        }
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrandType.CLIENT_CREDENTIALS.name().toLowerCase())
                .add("client_id", Contants.APP_ID)
                .add("tenant_id",Contants.TENANT_ID)
                .build();
       

        Request request = new Request.Builder()
                .addHeader("Content-Type", Contants.POST_CONTENT_TYPE)
                .addHeader("Authorization", authorizationBasic)
                .url(Contants.AUTH_TOKEN_API)
                .post(body)
                .build();
    //  System.out.println(request.toString ());
//       System.out.println(okHttpClient.newCall(request)
//               .execute().body().string());

        JSONObject jsonObject = (JSONObject) JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class).getEntity();
   //   System.out.println(jsonObject);
      //  System.out.println("jsonObject = " + jsonObject.toJSONString());
//        doSetToken((String) jsonObject.get("access_token"));

//        return doGetToken();
        return (String) jsonObject.get("access_token");
    }
    /**
     * 璁块棶鍙椾繚鎶ょ殑璧勬簮
     *
     * @param accessToken    token
     * @param tenantId tenantId
     * @param pageNum  椤垫暟
     * @param pageSize 涓?椤靛灏戞潯
     * @return Page<Person>
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings({"SameParameterValue", "noinspection unchecked", "ConstantConditions"})
    public static Page<Person> getPerson(String accessToken, int tenantId, int pageNum, int pageSize, String organizationId) throws Exception {
        Request request = new Request.Builder()
//                璋冪敤鎺ュ彛鎵?闇?瑕佺殑瀹夊叏楠岃瘉
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.RESOURCE_PERSON_API + "?page=" + pageNum + "&size=" + pageSize+ "&organizationId="+organizationId )
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
            accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
//                璋冪敤鎺ュ彛鎵?闇?瑕佺殑瀹夊叏楠岃瘉
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.RESOURCE_PERSON_API + "?page=" + pageNum + "&size=" + pageSize)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return JSONObject.parseObject(entityResponse.getEntity().toString(), Page.class);
    }
    public static Person getPersonByNo(String accessToken, int tenantId, int pageNum, int pageSize, String no) throws Exception {
    	
        Request request = new Request.Builder()
//                璋冪敤鎺ュ彛鎵?闇?瑕佺殑瀹夊叏楠岃瘉
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.RESOURCE_PERSON_API + "?page=" + pageNum + "&size=" + pageSize + "&no=" + no)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
//                璋冪敤鎺ュ彛鎵?闇?瑕佺殑瀹夊叏楠岃瘉
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.RESOURCE_PERSON_API + "?page=" + pageNum + "&size=" + pageSize + "&no=" + no)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        Page<Person> pagePerson = JSONObject.parseObject(entityResponse.getEntity().toString(), Page.class);
        List<Person> list = pagePerson.getContent();
        String userId = "";
        Person person = null;
        if(list != null && !list.isEmpty()) {
        	person = JSONObject.parseObject(JSONObject.toJSONString(list.get(0)), Person.class);
        	
        }
        return person;
    }

    /**
     * 缂撳瓨token
     *
     * @param accessToken  token
     * @param refreshToken refreshToken
     */
    @SuppressWarnings("unused")
    private static void cacheToken(String accessToken, String refreshToken) {
        //灏唗oken缂撳瓨鍒皉edis鎴栬?卻ession涓? 涓嬫鐩存帴鍦ㄧ紦瀛樹腑鍘籺oken
    }

    /**
     * token澶辨晥 鐢╮efreshToken閲嶆柊鎹㈠彇token 骞惰繘琛岀紦瀛?
     * 涓嶇敤姣忎竴娆¤闂彈淇濇姢鐨勮祫婧愰兘鍘诲埛鏂皌oken
     *
     * @param refreshToken refreshToken
     * @param basic        銆?basic
     * @return Map
     * @throws Exception 銆?鎶涘嚭寮傚父
     */
    @SuppressWarnings({"ConstantConditions", "unused"})
    private static Map<String, String> exchangeToken(String refreshToken, String basic) throws Exception {
        Map<String, String> map = new HashMap<>();
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrandType.REFRESH_TOKEN.name().toLowerCase())
                .add("refresh_token", refreshToken).build();
        Request request = new Request.Builder()
                .addHeader("Content-Type", Contants.POST_CONTENT_TYPE)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", basic)
                .url(Contants.AUTH_TOKEN_API)
                .post(body)
                .build();
        JSONObject jsonObject = (JSONObject) JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class).getEntity();
        map.put("access_token", (String) jsonObject.get("access_token"));
        map.put("refresh_token", (String) jsonObject.get("refresh_token"));
        return map;
    }
/////////////////////////////
    
    public static EntityResponse deletePerson(String username, String accessToken, Integer tenantId, String no) throws Exception {
      
        Map<Person.ResourceType, List<String>> map = new HashMap<>();

//        Person person = new Person();
//        person.setName(username);
//        person.setNo(no);
//        person.
//        person.setAvatars(map);
        JSONArray jsonarray = new JSONArray();
        jsonarray.add(no);
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), JSONObject.toJSONString(jsonarray));
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.DELETE_PERSON_API)
                .delete(body)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.DELETE_PERSON_API)
                    .delete(body)
                    .build(); 
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }
///////////////////////////////////////////////////////
    
    
    
    
 public static void batchDeletePerson()  throws Exception {
    	
	    String userName= "高测试人"; // will be verified later
    	File delIDFile = new File("C:\\temp\\del.txt");
        BufferedReader br = new BufferedReader(new FileReader(delIDFile));
               
        String s = null;
        String userIDDel;
        Person person = null;
    
        while((s = br.readLine())!=null){
        	 StringBuffer ID2Del = new  StringBuffer(s); 
            System.out.println("ID2Del = " + ID2Del);
         //   person = getPersonByNo(token, 3, 0, 10, ID2Del.toString());
//          
         // System.out.println("Person = " + person.toString());
         // System.out.println("Person = " + JSONObject.toJSONString(person));
//          
//          System.out.println("Person Name = " + person.getName());
//          System.out.println("Person no = " + person.getNo());
//          
        //  System.out.println("Person id = " + person.getId());
          if( person != null ) {  
	          String deleteID = person.getId();
	        //  System.out.println("Person id = " +  deleteID);
              EntityResponse deletePersonResponse = deletePerson(userName, token,  3, deleteID);
            
          }
        }
        br.close();   
        	
    }









    
    
    
////////////////////////////////////////////////////////    
  
    /**
     * 淇濆瓨浜哄憳淇℃伅
     * 编号不能重复
     * @param accessToken token
     * @param tenantId    绉熸埛id
     *
     * @return EntityResponse
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings({"ConstantConditions", "SameParameterValue"})
    public static EntityResponse getRecord(String accessToken, Integer tenantId, Integer page, Integer size ) throws Exception {
      
        Map<Person.ResourceType, List<String>> map = new HashMap<>();

//        JSONObject jo = new JSONObject();
//        jo.put("page", pageNumber);
//        jo.put("size", pageSize);
//        String qryJson = jo.toJSONString();
        String qryString = Contants.GET_ACCESSRECORD_API + "?page=" + page +"&size=" + size;
     //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546402357110&endTime=1546402719157";
        
    //    String qryString = Contants.GET_ACCESSRECORD_API +  "?regexName=false&startTime=1546402357110&endTime=1546402719157&page=0&size=10";
        
      //  startTime
     //   String qryString = Contants.GET_ACCESSRECORD_API + "?page=" + page +"&size=" + size + "&startTime=1546167858476";
     //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546167855033";
        //1546167855033
       // 1546167858476
     
        
      //  RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), qryJson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
              //  .url(Contants.GET_ACCESSRECORD_API+ "?sn=FC231118380001")
               // .url(Contants.GET_ACCESSRECORD_API+ "?startTime=1546167855033")
               // .url(Contants.GET_ACCESSRECORD_API)
                .url(qryString)
                //.get(body)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
            doSetToken(null);
            token = doGetToken();
            token = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    //.url(Contants.GET_ACCESSRECORD_API)
                    .url(qryString)
                   // .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }
    
////////////////////////////////////////////////////////
    
    @SuppressWarnings({"ConstantConditions", "SameParameterValue"})
    public static EntityResponse getRecordByTimeStamp(String accessToken, Integer tenantId, Integer page, Integer size, String startTimeStamp, String endTimeStamp, String resultType ) throws Exception {
      
        Map<Person.ResourceType, List<String>> map = new HashMap<>();

//        JSONObject jo = new JSONObject();
//        jo.put("page", pageNumber);
//        jo.put("size", pageSize);
//        String qryJson = jo.toJSONString();
     //   String qryString = Contants.GET_ACCESSRECORD_API + "?page=" + page +"&size=" + size;
        String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=" + startTimeStamp + "&endTime=" + endTimeStamp + "&page=" + page +"&size=" + size +"&resultType= " +resultType;
        
    //    String qryString = Contants.GET_ACCESSRECORD_API +  "?regexName=false&startTime=1546402357110&endTime=1546402719157&page=0&size=10";
        
      //  startTime
     //   String qryString = Contants.GET_ACCESSRECORD_API + "?page=" + page +"&size=" + size + "&startTime=1546167858476";
     //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546167855033";
        //1546167855033
       // 1546167858476
     
        
      //  RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), qryJson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
              //  .url(Contants.GET_ACCESSRECORD_API+ "?sn=FC231118380001")
               // .url(Contants.GET_ACCESSRECORD_API+ "?startTime=1546167855033")
               // .url(Contants.GET_ACCESSRECORD_API)
                .url(qryString)
                //.get(body)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
            doSetToken(null);
            token = doGetToken();
            token = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    //.url(Contants.GET_ACCESSRECORD_API)
                    .url(qryString)
                   // .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }



    public static EntityResponse GetDevice(String accessToken, int pageNum, int pageSize, Integer tenantId)throws Exception{
        Map<Person.ResourceType, List<String>> map = new HashMap<>();

//        JSONObject jo = new JSONObject();
//        jo.put("page", pageNumber);
//        jo.put("size", pageSize);
//        String qryJson = jo.toJSONString();
        String qryString = Contants.SN_DEVICE ;
        //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546402357110&endTime=1546402719157";

        //    String qryString = Contants.GET_ACCESSRECORD_API +  "?regexName=false&startTime=1546402357110&endTime=1546402719157&page=0&size=10";

        //  startTime
        //   String qryString = Contants.GET_ACCESSRECORD_API + "?page=" + page +"&size=" + size + "&startTime=1546167858476";
        //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546167855033";
        //1546167855033
        // 1546167858476


        //  RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), qryJson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                //  .url(Contants.GET_ACCESSRECORD_API+ "?sn=FC231118380001")
                // .url(Contants.GET_ACCESSRECORD_API+ "?startTime=1546167855033")
                // .url(Contants.GET_ACCESSRECORD_API)
                .url(qryString+ "?page=" + pageNum + "&size=" + pageSize)
                //.get(body)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute ().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
            doSetToken(null);
            token = doGetToken();
            token = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    //.url(Contants.GET_ACCESSRECORD_API)
                    .url(qryString+ "?page=" + pageNum + "&size=" + pageSize)
                    // .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;



    }

    public static EntityResponse getRule(String accessToken, Integer tenantId ) throws Exception {

        Map<Person.ResourceType, List<String>> map = new HashMap<>();

//        JSONObject jo = new JSONObject();
//        jo.put("page", pageNumber);
//        jo.put("size", pageSize);
//        String qryJson = jo.toJSONString();
        String qryString = Contants.RULE_API ;
        //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546402357110&endTime=1546402719157";

        //    String qryString = Contants.GET_ACCESSRECORD_API +  "?regexName=false&startTime=1546402357110&endTime=1546402719157&page=0&size=10";

        //  startTime
        //   String qryString = Contants.GET_ACCESSRECORD_API + "?page=" + page +"&size=" + size + "&startTime=1546167858476";
        //   String qryString = Contants.GET_ACCESSRECORD_API + "?startTime=1546167855033";
        //1546167855033
        // 1546167858476


        //  RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), qryJson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                //  .url(Contants.GET_ACCESSRECORD_API+ "?sn=FC231118380001")
                // .url(Contants.GET_ACCESSRECORD_API+ "?startTime=1546167855033")
                // .url(Contants.GET_ACCESSRECORD_API)
                .url(qryString)
                //.get(body)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
            doSetToken(null);
            token = doGetToken();
            token = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    //.url(Contants.GET_ACCESSRECORD_API)
                    .url(qryString)
                    // .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }

    /////////////////////////////////////

    /**
     * 淇濆瓨浜哄憳淇℃伅
     * 编号不能重复
     * @param accessToken token
     * @param tenantId    绉熸埛id
     * @param file        鐓х墖
     * @return EntityResponse
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings({"ConstantConditions", "SameParameterValue"})
    public static EntityResponse savePerson(String username, String accessToken, Integer tenantId, File file, String no, int OrganizationId, String wgNumber, String icnNumber, String idCard) throws Exception {
    	
    	Map<Person.ResourceType, List<String>> map = new HashMap<>();
        List<String> imageUrlList = new ArrayList<>();
//        String imageUrl = uploadImage(Contants.SAVE_PERSON_IMAGE_API, file, accessToken, tenantId);
    	EntityResponse<Entity> entityRes = uploadImageV21(Contants.V21_UPLOAD_IMAGE,file,accessToken,tenantId);
        if(entityRes.getStatus() == 200) {
	        if(entityRes.getEntity() != null) {
	        	//System.out.println(JSONObject.toJSONString(entityRes));
	        	List<JSONObject> list = (List<JSONObject>)entityRes.getEntity();
	        	if(!list.isEmpty()) {
	        		if(list.get(0).getBoolean("flag") == true) {
	        			//        		上传照片成功
	        			        		if(list.get(0).get("url") != "" && list.get(0).get("url") != null) {
	        			        			imageUrlList.add((String)list.get(0).get("url"));
	        			        		//	System.out.println("上照片");
	        			        		}else {
	        			        		 //   System.out.println("下一");
// 	上传照片失败
	        			        			return entityRes;
                                        }
	        			        	}else {
	        			//        		上传照片失败
	        			        		return entityRes;
	        			        	}
	        	}else {
	        		return entityRes;
	        	}
	        	
	        }else {
//	        	上传照片失败
	        	return entityRes;
	        }
        }else {
//        	上传照片失败
        	return entityRes;
        }
        
//        if(imageUrl != "" || imageUrl != null) {
//        	imageUrlList.add(imageUrl);
//        }
        map.put(Person.ResourceType.VISIBLE_LIGHT, imageUrlList);
        Person person = new Person ();
        person.setName(username);
        person.setNo(no);
        person.setWgNumber (wgNumber);
        person.setIcNumber (icnNumber);
        person.setAvatars(map);
        person.setOrganizationId(OrganizationId);
        person.setIdCard (idCard);
        String personJson = JSONObject.toJSONString(person);
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), personJson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.SAVE_PERSON_API)
                .post(body)
                .build();
        EntityResponse<Map<String,Object>> entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.SAVE_PERSON_API)
                    .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }

    /**
     * 淇敼浜哄憳淇℃伅
     * 修改人员时不能修改人员编号
     * @param accessToken token
     * @param tenantId    绉熸埛id
     * @param file        鐓х墖

     * @return EntityResponse
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings({"SameParameterValue", "ConstantConditions"})
    public static EntityResponse updatePerson(String accessToken, Integer tenantId,
                                              File file, Person person) throws Exception {
    	
//        String imageUrl = uploadImage(Contants.SAVE_PERSON_IMAGE_API, file, accessToken, tenantId);
        Map<Person.ResourceType, List<String>> map = new HashMap<>();
        List<String> imageUrlList = new ArrayList<>();
        // EntityResponse<Entity> entityRes = uploadImageV21(Contants.V21_UPLOAD_IMAGE,file,accessToken,tenantId);
//        if(entityRes.getStatus() == 200) {
//	        if(entityRes.getEntity() != null) {
//	        	//System.out.println(JSONObject.toJSONString(entityRes));
//	        	List<JSONObject> list = (List<JSONObject>)entityRes.getEntity();
//	        	if(!list.isEmpty()) {
//	        		if(list.get(0).getBoolean("flag") == true) {
//	        			//        		上传照片成功
//	        			        		if(list.get(0).get("url") != "" && list.get(0).get("url") != null) {
//	        			        			imageUrlList.add((String)list.get(0).get("url"));
//	        			        		}else {
////	        			        			上传照片失败
//	        			        			return entityRes;
//	        			        		}
//	        			        	}else {
//	        			//        		上传照片失败
//	        			        		return entityRes;
//	        			        	}
//	        	}else {
//	        		return entityRes;
//	        	}
//
//	        }else {
////	        	上传照片失败
//	        	return entityRes;
//	        }
//        }else {
////        	上传照片失败
//        	return entityRes;
//        }
////        imageUrlList.add(imageUrl);
//        map.put(Person.ResourceType.VISIBLE_LIGHT, imageUrlList);
//        Person person = new Person();
//        person.setName("杨加");
//        person.setNo(no); // employeenumber
//        person.setAvatars(map); //
        person.setAvatars(map);
        String personJson = JSONObject.toJSONString(person);
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), personJson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.UPDATE_PERSON_API + person.getId())
                .post(body)
                .build();
        EntityResponse entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.UPDATE_PERSON_API + person.getId())
                    .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }

    /**
     * 娣诲姞鍜屼慨鏀逛汉鍛樻椂涓婁紶鐓х墖  2.0老算法
     *
     * @param uploadUrl   涓婁紶鐨勮矾寰?
     * @param file        鐓х墖
     * @param accessToken token
     * @param tenantId    绉熸埛id
     * @return String
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings("ConstantConditions")
    private static String uploadImage(String uploadUrl, File file, String accessToken, Integer tenantId) throws Exception {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "1.jpg", RequestBody.create(
                        MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(uploadUrl)
                .post(requestBody)
                .build();
        EntityResponse entityResponse  = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            MultipartBody.Builder builderReset = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "1.jpg", RequestBody.create(
                            MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
            RequestBody requestBodyReset = builderReset.build();
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(uploadUrl)
                    .post(requestBodyReset)
                    .build();
            entityResponse  = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return (String) (entityResponse).getEntity();
    }

    /**
     * 澧炲姞瑙勫垯骞剁粦瀹氳澶?
     *
     * @param accessToken token
     * @param tenantId    绉熸埛id
     * @return EntityResponse
     * @throws Exception 鎶涘嚭寮傚父
     */
    // person rule bind
    @SuppressWarnings({"ConstantConditions", "SameParameterValue"})

    public static EntityResponse addRuleAndIssueDevice(String accessToken, Integer tenantId, JSONArray userId, String Sn) throws Exception {
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), JSONObject.toJSONString(setJsonObject(userId)));
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.ADD_RULE_API)
                .post(body)
                .build();
        EntityResponse addRuleResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        
       // System.out.println("addrullresponese = =" + addRuleResponse );
        
        if (addRuleResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.ADD_RULE_API)
                    .post(body)
                    .build();
            addRuleResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
         //   System.out.println("addrullresponese = =" + addRuleResponse );
            JSONObject addRuleResponseEntity = (JSONObject) addRuleResponse.getEntity();
            return deviceIssueRule((Integer) addRuleResponseEntity.get("id"), tenantId, token,Sn);
        }
        JSONObject addRuleResponseEntity = (JSONObject) addRuleResponse.getEntity();
        return deviceIssueRule((Integer) addRuleResponseEntity.get("id"), tenantId, accessToken,Sn);
    }
    public static EntityResponse addRuleAndIssueDevices(String accessToken, Integer tenantId, String userId, String no, String startDate, String endDate, String passMode) throws Exception {
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), JSONObject.toJSONString(setTimeRule(userId, startDate, endDate, passMode)));
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.ADD_RULE_API)
                .post(body)
                .build();
        EntityResponse addRuleResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        System.out.println(addRuleResponse.toString ());
        if (addRuleResponse.getStatus() == 401){
            accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.ADD_RULE_API)
                    .post(body)
                    .build();
            addRuleResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
            System.out.println("addrullresponese = =" + addRuleResponse );
            JSONObject addRuleResponseEntity = (JSONObject) addRuleResponse.getEntity();
            return deviceIssueRules((Integer) addRuleResponseEntity.get("id"), tenantId, token);
        }
        JSONObject addRuleResponseEntity = (JSONObject) addRuleResponse.getEntity();
        System.out.println("addRuleResponseEntity------"+addRuleResponseEntity.get("id").toString());
        return deviceIssueRules(Integer.parseInt(addRuleResponseEntity.get("id").toString()), tenantId, accessToken);
    }
    private static JSONObject setTimeRule(String userId, String startDate, String endDate, String passMode) {
        JSONObject jsonObject = new JSONObject();
        JSONArray personIds = new JSONArray();
        personIds.add(userId);
        Integer[] customDays = new Integer[]{1, 2, 3, 4, 5, 6, 7};

        JSONArray timeRule = new JSONArray();
        JSONObject title = new JSONObject();
//        同一规则内不能重名
        title.put("title", userId);
        title.put("action", "ALLOW");
        title.put("dayType", "CUSTOM");
        title.put("type", "TIMING");
        title.put("customDays", customDays);

        System.out.println(""+startDate.substring(6, 8)+"---------"+startDate.substring(4, 6)+"------"+startDate.substring(0, 4));
        JSONObject dayRange = new JSONObject();
        JSONObject dayRangeStart = new JSONObject();
        dayRangeStart.put("day", startDate.substring(6, 8));
        dayRangeStart.put("mm", startDate.substring(4, 6));
        dayRangeStart.put("year", startDate.substring(0, 4));
        JSONObject dayRangeEnd = new JSONObject();
        dayRangeEnd.put("day", endDate.substring(6, 8));
        dayRangeEnd.put("mm", endDate.substring(4, 6));
        dayRangeEnd.put("year", endDate.substring(0, 4));
        dayRange.put("start", dayRangeStart);
        dayRange.put("end", dayRangeEnd);
        JSONObject timeRange = new JSONObject();
        JSONObject timeRangeStart = new JSONObject();
        timeRangeStart.put("hour", "00");
        timeRangeStart.put("minute", "00");
        timeRangeStart.put("second", "01");
        JSONObject timeRangeEnd = new JSONObject();
        timeRangeEnd.put("hour","23");
        timeRangeEnd.put("minute", "59");
        timeRangeEnd.put("second", "59");
        timeRange.put("start", timeRangeStart);
        timeRange.put("end", timeRangeEnd);
        title.put("timeRange", timeRange);
        title.put("dayRange", dayRange);
//        不能重复
        jsonObject.put("name", "qe" + System.currentTimeMillis());
        jsonObject.put("passMode", passMode);//人脸验证模式
        jsonObject.put("personIds", personIds);
        jsonObject.put("timeRule", timeRule);
        timeRule.add(title);
        return jsonObject;
    }

    /**
     * 璁惧缁戝畾瑙勫垯FV
     *
     * @param ruleId      瑙勫垯id
     * @param tenantId    绉熸埛id
     * @param accessToken token
     * @return EntityResponse
     * @throws Exception 鎶涘嚭寮傚父
     */
    @SuppressWarnings("ConstantConditions")
    
    // 下发 规则
    private static EntityResponse deviceIssueRule(Integer ruleId, Integer tenantId, String accessToken, String SN) throws Exception {
        JSONArray jsonArray = new JSONArray();
      // jsonArray.add("FC221118370005");
      //  FC231118380001
       /* jsonArray.add("FC232218490059");
        jsonArray.add("FC232218490036");
        jsonArray.add("FC232218490058");*/


        jsonArray.add(SN);
        jsonArray.add("H2033319020037");
      //  jsonArray.add("");
      /*  jsonArray.add("FC232218490038");
        jsonArray.add("H2033319020052");
        jsonArray.add("H2033319020033");
        jsonArray.add("H2013319020036");
        jsonArray.add("H2033319020020");
        jsonArray.add("H2033319020021");
        jsonArray.add("H2033319020037");
        jsonArray.add("H2033319020024");
*/
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), JSONObject.toJSONString(jsonArray));
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.DEVICE_RULE_ISSUE_API + ruleId)
                .post(body)
                .build();
        EntityResponse entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
     System.out.println("下发 = ==" + entityResponse );
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.DEVICE_RULE_ISSUE_API + ruleId)
                    .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }


    private static EntityResponse deviceIssueRules(Integer ruleId, Integer tenantId, String accessToken) throws Exception {
        JSONArray jsonArray = new JSONArray();
        // jsonArray.add("FC221118370005");
        //  FC231118380001
        //设备列表
      /*  for(int i = 0; i < ConstantsFace.DEVICE_ID_ARRAY.length; i++){
            jsonArray.add(ConstantsFace.DEVICE_ID_ARRAY[i]);
        }*/
//        jsonArray.add("FC232218490059");
//        jsonArray.add("FC232218490036");
//        jsonArray.add("FC232218490058");
        jsonArray.add("FC242218490032");
   /*     jsonArray.add("H2033319020037");
        jsonArray.add("H2033319020141");

*/
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), JSONObject.toJSONString(jsonArray));
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(Contants.DEVICE_RULE_ISSUE_API + ruleId)
                .post(body)
                .build();
        EntityResponse entityResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        System.out.println("entityResponse = ==" + entityResponse );
        if (entityResponse.getStatus() == 401){
            accessToken = getToken(basic);
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(Contants.DEVICE_RULE_ISSUE_API + ruleId)
                    .post(body)
                    .build();
            entityResponse = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }



    public static EntityResponse AddOrg(String accessToken, String parentOraId, String OrganationName) throws Exception {
            JSONObject jsonObject=new JSONObject();
            Organation org = new Organation();
             org.setName (OrganationName);
             org.setParentId (parentOraId);
        String orgjson = JSONObject.toJSONString(org);
      //  System.out.println(orgjson);
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON),orgjson);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(Contants.TENANT_ID))
                .url(Contants.Org_Save)
                .post(body)
                .build();
        EntityResponse addRuleResponse = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        return addRuleResponse;
    }
    public static EntityResponse Device_Reboot(String accessToken, String sn) throws Exception {
        JSONArray jsonArray=new JSONArray();
        jsonArray.add (sn);
        String sns=jsonArray.toString ();
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON),sns);
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
//                鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(Contants.TENANT_ID))
                .url(Contants.Device_Reboot)
                .post(body)
                .build();
        EntityResponse Device = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        return Device;
    }
   /* public static void main(String[] args) throws Exception {
       // int tenantId = 2;
        String userName= "高测试人";
        String employeenumber = "494239";
        Person person = null;
		String url = "C:\\temp\\head.jpg";
	//	Img2Base64Util.generateImage(updatePicBase64,url);
		File file = new File(url);
        
        
//        鑾峰彇token
        token = getToken(basic);
        
        ///////////////////start of uploading picture////////////////////
        
//        EntityResponse<Entity> entityRes = uploadImageV21(Contants.V21_UPLOAD_IMAGE,file,token,tenantId);
//        if(entityRes.getStatus() == 200) {
//        	
//        
//	        if(entityRes.getEntity() != null) {
//	        	System.out.println(JSONObject.toJSONString(entityRes));
//	        	if(entityRes.getEntity().getFlag() == true) {
//	//        		上传照片成功
//	        		
//	        	}else {
//	//        		上传照片失败
//	        	}
//	        }
//        }else {
////        	上传照片失败
//        }
        //////////////////////// end of uploading picture//////////////////////////////
        

//        String entity = uploadImage(Contants.SAVE_PERSON_IMAGE_API,file,token,tenantId);
     //   public static EntityResponse deletePerson(String username,  String accessToken, Integer tenantId,  String no)

//        batchDeletePerson();
        
        ////////////////start of deleting one person
        //public static Person getPersonByNo(String accessToken, int tenantId, int pageNum, int pageSize,String no)
//        person = getPersonByNo(token, 3, 0, 10, employeenumber);
//        
//        System.out.println("Person = " + person.toString());
//        System.out.println("Person = " + JSONObject.toJSONString(person));
//        
//        System.out.println("Person Name = " + person.getName());
//        System.out.println("Person no = " + person.getNo());
//        
//        System.out.println("Person id = " + person.getId());
//        String deleteID = person.getId();
////        
//        
//            EntityResponse deletePersonResponse = deletePerson(userName, token,  3, deleteID);          
        //   System.out.println("delete person =====> " + JSONObject.toJSONString(deletePersonResponse));
        
        ///////////////////////////end of deleting person
        
          //public static EntityResponse getRecord(String accessToken, Integer tenantId, File file, String no)
        
       /////////////////////////////   to get the record ////////////
           userAccessRecordsDump2File();
          
         //  userAccessRecordsDump2Excel();
           
//       EntityResponse recordResponse = getRecord(token, 3, 1, 10);   
//        
//        System.out.println("record =====> " + JSONObject.toJSONString(recordResponse));
         ///////////////////////////////end of get the record/////// 
    
        
        
        
        
//
//        Page<Person> personPage = getPerson(token, tenantId, 0, 10);
//        System.out.println("浜哄憳鍒楄〃淇℃伅 =====> " + JSONObject.toJSONString(personPage));
//        String no = VisitSaasUtil.randomNextInt();
//        File file = new File("C:/Users/Administrator/Desktop/1.jpg");
//        EntityResponse savePersonResponse = savePerson(token, tenantId, file, no);
//        System.out.println("娣诲姞骞惰繑鍥炵殑浜哄憳淇℃伅 =====> " + JSONObject.toJSONString(savePersonResponse));
//        JSONObject jsonObject = (JSONObject) savePersonResponse.getEntity();
//
//        EntityResponse updatePersonResponse = updatePerson(token, tenantId, file, (String) jsonObject.get("id"),
//                (String) jsonObject.get("no"));
//        System.out.println("淇敼浜哄憳杩斿洖鐨勪俊鎭? =====> " + JSONObject.toJSONString(updatePersonResponse));
//        EntityResponse addRuleAndIssueDevice = addRuleAndIssueDevice(token, tenantId, (String) jsonObject.get("id"), no);
//        System.out.println("娣诲姞瑙勫垯骞剁粦瀹氳澶? =====> " + JSONObject.toJSONString(addRuleAndIssueDevice));

     //   494239
        
    }
*/


        //单图片特征提取
    public static String  faceFeatureExtract(String accessToken,String fileUrl, Integer tenantId) throws Exception {
        File file = new File(fileUrl);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //  .addFormDataPart("faceImages", "2.jpg", RequestBody.create(
                .addFormDataPart("faceImages", fileUrl, RequestBody.create(
                        MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .addHeader("appId", Contants.APP_ID)
                .url(Contants.FACE_FEATURE_API)
                .post(requestBody)
                .build();
        String result =  okHttpClient.newCall(request).execute().body().string();
        return result;
    }


    public static String  faceFeatureF(String fileUrl, Integer tenantId) throws Exception {
        File file = new File(fileUrl);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //  .addFormDataPart("faceImages", "2.jpg", RequestBody.create(
                .addFormDataPart("faceImages", fileUrl, RequestBody.create(
                        MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()

                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                .addHeader("tenantId", String.valueOf(tenantId))
                .addHeader("appId", Contants.APP_ID)
                .url(Contants.FACE_QUALITY_API)
                .post(requestBody)
                .build();
        String result =  okHttpClient.newCall(request).execute().body().string();

        return result;
    }


            //特质对比
    public static String faceFeatureCompare(String accessToken,String feature1, String feature2, Integer tenantId) throws Exception {
        JSONArray jsonarray = new JSONArray();
        jsonarray.add(feature1);
        jsonarray.add(feature2);
        JSONObject jo = new JSONObject();
        jo.put("featureA", feature1);
        jo.put("featureB", feature2);
        RequestBody body = FormBody.create(MediaType.parse(Contants.POST_CONTENT_TYPE_JSON), JSONObject.toJSONString(jo));
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_JSON)
////               鐢宠鎺堟潈鐨勫畨鍏ㄩ獙璇?
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
//               .addHeader("appId", "pkuface")

                .url(Contants.FACE_FEATURE_COMPARE_API)
                .post(body)
                .build();
        String result =  okHttpClient.newCall(request).execute().body().string();

        return result;
    }
        public static int getOrgId(String orgname) throws  Exception{
                int orgid=0;
                EntityResponse rule = getRule (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic), Integer.parseInt (Contants.TENANT_ID));
           //    System.out.println(rule);
                if(rule!=null){
                    JSONArray jo = JSONObject.parseArray (new String(rule.getEntity ().toString ()));
                    for (int i = 0; i < jo.size (); i++) {
                        JSONObject jsonObj = jo.getJSONObject(i);
                        JSONArray id = jsonObj.getJSONArray ("childNodes");
                        for(int j=0;j<id.size ();j++){
                            JSONObject jsonObj1 = id.getJSONObject(j);
                            String name=jsonObj1.getString ("name");
                              if(name==orgname || name.equals (orgname)){
                                  orgid=Integer.parseInt (jsonObj1.getString ("id"));
                                  break;
                              }
                        }
                    }
                }
            return orgid;
        }

    // 查询照片特征
    public static String  faceTest(String accessToken,File file, Integer tenantId) throws Exception {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //  .addFormDataPart("faceImages", "2.jpg", RequestBody.create(
                .addFormDataPart("faceImages", "", RequestBody.create(
                        MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .addHeader("appId", Contants.APP_ID)
                .url(Contants.FACE_OPEN_FEATURE)
                .post(requestBody)
                .build();
        String result =  okHttpClient.newCall(request).execute().body().string();
        return result;
    }




    // 返回照片特征信息
    public static String getFeature(String responseStr) throws ThePicHasNotFaceException {
        JSONObject json = JSONObject.parseObject(responseStr);
        JSONObject entity = json.getJSONArray("entity").getJSONObject(0);

        if(entity.getBoolean("hasFace")){
            return   entity.getJSONObject("faceFeature")
                    .getString("feature");
        }else{
            throw new ThePicHasNotFaceException("未检测到人脸信息，请确认后再上传!");
        }

    }



    public static void main(String[]args) throws Exception  {
        //获取token
        OkHttpClient client = new OkHttpClient();

        VisitSAASAuthorize visitSAASAuthorize=new VisitSAASAuthorize();
        try {

            String token = VisitSAASAuthorize.getToken (visitSAASAuthorize.basic);
            System.out.println(token);

            //Person person = VisitSAASAuthorize.getPersonByNo(token, Integer.parseInt(Contants.TENANT_ID), 0 , 10 , "700200");
            String picPath = "C:\\Users\\Administrator\\Desktop\\pic\\1.jpg";
            int tent_id = 1;
            String res = VisitSAASAuthorize.faceTest(token, new File(picPath), tent_id);
            String feature1 = visitSAASAuthorize.getFeature(res);

            String picPath2 = "C:\\Users\\Administrator\\Desktop\\pic\\2.jpg";
            String res2 = VisitSAASAuthorize.faceTest(token, new File(picPath2), tent_id);
            String feature2 = visitSAASAuthorize.getFeature(res2);

//
            // 特征对比
            String result = VisitSAASAuthorize.faceFeatureCompare(token, feature1, feature2, tent_id);

            System.out.println(JSONObject.parseObject(result).getString("entity"));







   /*JSONArray ids=new JSONArray();
            ids.add ("57d9a1427d6c47c389b87bb9455f7f25");
            ids.add ("48490652486440b1bd5d0b4c7e27794d");
             ids.add("729452f7e0d443eb8c62e829d04a14f5");
             ids.add ("46f6f9fc12d943bcaf764da064e14915");*/
   //ids.add ("df5063e507964e5e91bc844bfc05cd5b");
   //ids.add ("48b1b530a7244e6aa2a46c8a32233181");
 //  ids.add("b60dcb5eafff4364b997e485217ebea9");
  // ids.add ("d5b45cdd8f7f4cf881569fcb71f206fa");
         //   ids.add ("0fe6d12136544282baa6ef0670b2f7ee");
        //    ids.add("efabd9ac97d14ab09ca07334f0c911b9");
      /*      ids.add("c3c8bec13d874fdb8879d7453dbd6d89");
            ids.add("f783f987185241f8b37c2e2a6b3d19a0");
            ids.add("9f3fcc96cc304ee49a066a4d2eaba91f");
            ids.add("565fbcf5326749a6a57f9af9d9dcd4ea");
            ids.add("b410bba69f8949ac9f298c04853872b6");
            ids.add("169d7d8fb10b4c648d1ed415adce21db");
      */
      //9f88033b4f284588ba4a975e12246f31
//8ec23268bfcd4c3abb06240b2a4a15bc
 //c5d357e22120498da9e2a4906a98b606
            //011904000311
            //addRuleAndIssueDevice(visitSAASAuthorize.getToken(visitSAASAuthorize.basic),Integer.parseInt (Contants.TENANT_ID),ids,"H2033319020141")
         //  addRuleAndIssueDevices (visitSAASAuthorize.getToken(visitSAASAuthorize.basic),Integer.parseInt (Contants.TENANT_ID),"3a4f6b13b9904d21aa4b6d29ed9b695f","3a4f6b13b9904d21aa4b6d29ed9b695f","20191023","20191031","FACE_AND_ID");

        } catch (Exception e) {
            e.printStackTrace ();
        }

          /*  try {
                System.out.println(VisitSAASAuthorize.Device_Reboot (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic),"C1030231727048"));
            }catch (Exception e){
                e.printStackTrace ();
            }
*/




        try {
      //    EntityResponse rule = VisitSAASAuthorize.getRule (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic), Integer.parseInt (Contants.TENANT_ID));
     //   System.out.println(rule);
         //   System.out.println(rule);

//            String fa="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";
//            String fb="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";
//            String file = "C:\\Users\\user\\Desktop\\fe26b1990945cba37f053af0aaf1119.jpg";
//            String file2 = "C:\\Users\\user\\Desktop\\611a6573ea457dc93dca583b1b3219e.jpg";
//
//           // String s = VisitSAASAuthorize.faceFeatureExtract (visitSAASAuthorize.getToken (visitSAASAuthorize.basic),file2, 3);
//         String s=visitSAASAuthorize.faceFeatureCompare (visitSAASAuthorize.getToken (visitSAASAuthorize.basic),fa,fb,3);
//           System.out.println(s);

        }catch (Exception e){
            e.printStackTrace ();
        }



    /* try {
         EntityResponse ssss = VisitSAASAuthorize.AddOrg (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic), "3", "生成111");
         System.out.println(ssss);
     }catch (Exception e){
         e.printStackTrace ();
     }*/
    /*    try {
            String user="zhaolaoshi";
            String no="zhaolaoshi0003";
            String url="C:\\Users\\user\\Downloads\\233.jpg";
            File file=new File (url);
            String wgNumber="130124198402263316";
            String icNumber="130124198402263316";
            String idCard="130124198402263316";
            EntityResponse entityResponse = VisitSAASAuthorize.savePerson (user, getToken (basic), Integer.parseInt (Contants.TENANT_ID), file, no, 80,wgNumber,icNumber,idCard);
           System.out.println(entityResponse.getStatus ());
          } catch (Exception e) {
            e.printStackTrace ();
        }*/
        try {
// System.out.println(  VisitSAASAuthorize.getPersonByNo (getToken (basic),3,0,10,"1901111489").toString ());
    /*   Page<Person> person = VisitSAASAuthorize.getPerson (visitSAASAuthorize.getToken (visitSAASAuthorize.basic), Integer.parseInt (Contants.TENANT_ID),0, 100);
       System.out.println(person);*/
         } catch (Exception e) {
            e.printStackTrace ();
        }
        /*
        try {

        } catch (Exception e) {
            e.printStackTrace ();
        }*/
  /*     try {
            EntityResponse addRuleAndIssueDevice = addRuleAndIssueDevice(getToken (basic), 2, "", "");
            System.out.println("hu =====> " + JSONObject.toJSONString(addRuleAndIssueDevice));
        } catch (Exception e) {
            e.printStackTrace ();
        }*/
/*
      try {
        System.out.println(  VisitSAASAuthorize.getRecord (getToken (basic),2,0,5));
      }catch (Exception e){
          e.printStackTrace ();

      }finally {

      }*/

        /*try {
            EntityResponse rule = VisitSAASAuthorize.getRule (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic), Integer.parseInt (Contants.TENANT_ID));
            System.out.println(rule);
        }catch (Exception e){
            e.printStackTrace ();
        }*/
           /* try {
                EntityResponse rule = VisitSAASAuthorize.getRule (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic), Integer.parseInt (Contants.TENANT_ID));
               System.out.println(rule);
                  JSONArray jo = JSONObject.parseArray (new String(rule.getEntity ().toString ()));
               System.out.println(jo);
                JSONObject jsonObject = jo.getJSONObject (0);
               System.out.println(jsonObject.getString ("id"));
               *//* for (int i = 0; i < jo.size (); i++) {
                    JSONObject jsonObj = jo.getJSONObject(i);
                    JSONArray  id = jsonObj.getJSONArray ("childNodes");
                            for(int j=0;j<id.size ();j++){
                                JSONObject jsonObj1 = id.getJSONObject(j);
                                String name=jsonObj1.getString ("name");
                                System.out.println(name);
                            }
                }*//*
            }catch (Exception e){
            e.printStackTrace ();
            }*/
/*

        try {
            EntityResponse rule = VisitSAASAuthorize.GetDevice (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic), 0,9999,Integer.parseInt (Contants.TENANT_ID));
            JSONObject jsonObject=JSONObject.parseObject (new String(rule.getEntity ().toString ()));
            JSONArray jo=jsonObject.getJSONArray ("content");
            //  JSONArray jo = JSONObject.parseArray (new String(rule.getEntity ().toString ()),EntityResponse.class);
            System.out.println(jo);
           */
/* for (int i = 0; i < jo.size (); i++) {
                JSONObject jsonObj = jo.getJSONObject(i);

                JSONArray  id = jsonObj.getJSONArray ("childNodes");
                for(int j=0;j<id.size ();j++){
                    JSONObject jsonObj1 = id.getJSONObject(j);
                    String name=jsonObj1.getString ("name");
                    System.out.println(name);
                }
            }*//*

        }catch (Exception e){
            e.printStackTrace ();
        }
*/

         /*   try {
                System.out.println(VisitSAASAuthorize.getRecord (VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic),2,0,10));


            }catch (Exception e){
                e.printStackTrace ();
            }*/
        try {
         //   System.out.println(VisitSAASAuthorize.deletePerson ("汪秦森",VisitSAASAuthorize.getToken (VisitSAASAuthorize.basic),3,"44e69ffd8cbf4aa5a353669da835b32d"));
         }catch (Exception e){
             e.printStackTrace ();
         }



    }



    /**
     * 灏佽璇锋眰娣诲姞瑙勫垯鐨勫弬鏁?
     * 瑙勫垯鍚嶇О涓嶈兘閲嶅锛岄噸澶嶅垯鎶涘嚭寮傚父 瀛楁锛歯ame
     * 鍚屼竴瑙勫垯鍐呬笉鑳介噸澶? 瀛楁锛歵itle
     *
     * @return JSONObject
     */
//    private static JSONObject setJsonObject(String userId, String no) {
//        JSONObject jsonObject = new JSONObject();
//        JSONArray personIds = new JSONArray();
//        personIds.add(userId);
//        JSONArray timeRule = new JSONArray();
//        JSONArray customDays = new JSONArray();
//        customDays.add(1);
//        customDays.add(2);
//        customDays.add(3);
//        customDays.add(4);
//        customDays.add(5);
//        customDays.add(6);
//        customDays.add(7);
//        JSONObject title = new JSONObject();
//        jsonObject.put("customDays", customDays);
//
//        title.put("title", "s2s2556618" + no);
//        title.put("action", "ALLOW");
//        title.put("dayType", "CUSTOM");
//        title.put("type", "TIMING");
//        timeRule.add(title);
//        JSONObject timeRange = new JSONObject();
//        JSONObject dayRange = new JSONObject();
//        JSONObject dayRangeStart = new JSONObject();
//        dayRangeStart.put("day","22");
//        dayRangeStart.put("mm","11");
//        dayRangeStart.put("year","2018");
//        JSONObject dayRangeEnd = new JSONObject();
//        dayRangeEnd.put("day","24");
//        dayRangeEnd.put("mm","11");
//        dayRangeEnd.put("year","2018");
//        dayRange.put("start",dayRangeStart);
//        dayRange.put("end",dayRangeEnd);
//        JSONObject start = new JSONObject();
//        start.put("hour", "14");
//        start.put("minute", "27");
//        start.put("second", "32");
//        JSONObject end = new JSONObject();
//        end.put("hour", "20");
//        end.put("minute", "40");
//        end.put("second", "32");
//        timeRange.put("start", start);
//        timeRange.put("end", end);
//        title.put("timeRange", timeRange);
//        title.put("dayRange",dayRange);
////      名称不能重复
//        jsonObject.put("name",  no+ "g532"); // name of the rule
//        jsonObject.put("passMode", "FACE");
//        jsonObject.put("personIds", personIds);
//        jsonObject.put("timeRule", timeRule);
//        System.out.println("66666666666666666" + JSONObject.toJSON(jsonObject));
//        return jsonObject;
//    }
    private static JSONObject setJsonObject(JSONArray userIds) {
        JSONObject jsonObject = new JSONObject();

        Integer[] customDays = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        JSONArray timeRule = new JSONArray();
        JSONObject title = new JSONObject();
//        同一规则内不能重名
        title.put("title", "ssf44s");
        title.put("action", "ALLOW");
        title.put("dayType", "CUSTOM");
        title.put("type", "TIMING");
        title.put("customDays", customDays);

        JSONObject dayRange = new JSONObject();
        JSONObject dayRangeStart = new JSONObject();
        dayRangeStart.put("day", "22");
        dayRangeStart.put("mm", "12");
        dayRangeStart.put("year", "2018");
        JSONObject dayRangeEnd = new JSONObject();
        dayRangeEnd.put("day", "22");
        dayRangeEnd.put("mm", "12");
        dayRangeEnd.put("year", "2029");
        dayRange.put("start", dayRangeStart);
        dayRange.put("end", dayRangeEnd);
        JSONObject timeRange = new JSONObject();
        JSONObject timeRangeStart = new JSONObject();
        timeRangeStart.put("hour", "10");
        timeRangeStart.put("minute", "30");
        timeRangeStart.put("second", "32");
        JSONObject timeRangeEnd = new JSONObject();
        timeRangeEnd.put("hour", "20");
        timeRangeEnd.put("minute", "30");
        timeRangeEnd.put("second", "32");
        timeRange.put("start", timeRangeStart);
        timeRange.put("end", timeRangeEnd);
        title.put("timeRange", timeRange);
        title.put("dayRange", dayRange);
//        不能重复
        jsonObject.put("name", "qe" + System.currentTimeMillis());
        jsonObject.put("passMode", "FACE_AND_ID");
        jsonObject.put("personIds", userIds);
        jsonObject.put("timeRule", timeRule);
        timeRule.add(title);
        return jsonObject;
    }
    
    
    private static EntityResponse<Entity> uploadImageV21(String uploadUrl, File file, String accessToken, Integer tenantId) throws Exception {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "1.jpg", RequestBody.create(
                        MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("tenantId", String.valueOf(tenantId))
                .url(uploadUrl)
                .post(requestBody)
                .build();
        EntityResponse<Entity> entityResponse  = JSONObject.parseObject(okHttpClient.newCall(request)
                .execute().body().string(), EntityResponse.class);
        if (entityResponse.getStatus() == 401){
        	accessToken = getToken(basic);
            MultipartBody.Builder builderReset = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "1.jpg", RequestBody.create(
                            MediaType.parse(Contants.POST_CONTENT_TYPE_IMAGE), file));
            RequestBody requestBodyReset = builderReset.build();
            Request requestReset = new Request.Builder()
                    .addHeader("Content-type", Contants.POST_CONTENT_TYPE_DATA)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("tenantId", String.valueOf(tenantId))
                    .url(uploadUrl)
                    .post(requestBodyReset)
                    .build();
            entityResponse  = JSONObject.parseObject(okHttpClient.newCall(requestReset)
                    .execute().body().string(), EntityResponse.class);
        }
        return entityResponse;
    }
    //////////////////////
    
//////////////////////////////////
	 
	 
			public static void userAccessRecordsDump2File()  throws Exception {

			//	EntityResponse recordResponse = getRecord(token, 3, 2, 10);
			Integer index= 0;
			String employee_number;
			JSONObject jo_input = new JSONObject();
			JSONObject jo_object = new JSONObject();
			JSONObject jo_personInfo = new JSONObject();
			JSONObject jo_deviceinfo = new JSONObject();
			
			JSONObject jo_API_start= new JSONObject();;
			float  f_score;
			FileWriter fw_facerecord;
			fw_facerecord = new FileWriter("C:\\temp\\ccc.txt", true); 
//			FileWriter fw_WestGate;
//			FileWriter fw_SouthGate;
//			FileWriter fw_SouthWestGate;
			
//			String FilePicLib;
//			String FilePicCap;
			String format = "yyyy-MM-dd HH:mm:ss";
			
			int hour;
			
//			//jo_input.put("from", 0);
//			//jo_input.put("size", 10);
//			
//			//jo.put("startTime", "2018-03-16 12:00:05");
//			//jo.put("endTime", "2018-03-16 12:10:05");
//			//jo.put("areaId", "f42qe2r56qertgtasdfaqw45r");
//			//String target_date= "2018-10-31";
//			String target_date= "2018-10-30";
//			String  margin_date = "2018-10-31";
//			jo_input.put("startTime", "2018-10-30 00:00:01");
//			//jo_input.put("orderby", "desc");
//			jo_input.put("orderby", "asc");
//			//jo.put("personId", "pid");*/
//			//System.out.println(fr.userAccessRecords(jo_input));
//			jo_API_start = fr.userAccessRecords(jo_input);
			// System.out.println("record =====> " + JSONObject.toJSONString(recordResponse));
			
			
						
						
						
						//	fw_WestGate = new FileWriter("C:\\facerecord\\westgate01NOV.txt", true); 
						//fw_SouthWestGate = new FileWriter("C:\\facerecord\\southwestgate01NOV.txt", true); 
						//fw_SouthGate = new FileWriter("C:\\temp\\southgate30OCT.txt", true); 
						
			
			 for (index =0; index < 3; index++) {			
						
				 EntityResponse recordResponse = getRecord(token, 1, index, 10);
				  System.out.println("recordres =====> " + JSONObject.toJSONString(recordResponse));
				  System.out.println("entity =====> " + JSONObject.toJSONString(recordResponse.getEntity()));
					
					jo_API_start = JSONObject.parseObject((recordResponse.getEntity()).toString());
					System.out.println("jo_API_start =====> " +  jo_API_start.toJSONString());
										
					String qty = jo_API_start.getString("numberOfElements");
					
					//jo_object = jo_API_start.getJSONObject("content");
									
					System.out.println("qty =====> " + qty);
					
					
					// System.out.println("joarry =====> content" +  jo_API_start.getJSONArray("content"));
					
					System.out.println("joarry =====> content" +  jo_API_start.getJSONArray("content"));
					
					////////////////////////parse parameters///////////////////////////////
					JSONArray jsArry = new JSONArray();
					jsArry = jo_API_start.getJSONArray("content");
					JSONObject jo_array = new JSONObject();
					System.out.println("size of content = " + jsArry.size());
				  					
						for(int i= 0; i< jsArry.size(); i++) {
						
						
						//	jo_array = jsArry.getJSONObject(0);	
						jo_array = jsArry.getJSONObject(i);	
						String userName = jo_array.getString("name"); 
						String syscTime = jo_array.getString("syncTime"); 
						jo_personInfo = jo_array.getJSONObject("personInfo");
						jo_deviceinfo = jo_array.getJSONObject("deviceInfo");
						
						if(jo_personInfo != null && !jo_personInfo.isEmpty() )
						{
							 employee_number = jo_personInfo.getString("no");	
						}
						else {
							 employee_number = jo_array.getString("idNumber"); 
						}
						
						
						String face_score = jo_array.getString("score"); // must be greater than 0.63
						
						
						String areaName = jo_deviceinfo.getString("name"); 
						//String imageUri = jo_array.getString("imageUri");
						//String visibleLightPic = jo_array.getString("visibleLightPic");
						
						f_score = Float.valueOf(face_score);
						
						
						//System.out.println("imageUri = " + imageUri);		
						//	System.out.println("visibleLightPic = " + visibleLightPic);		
						
						System.out.println("userName = "+  userName  + "   employeeNumber = " + employee_number +"  syscTime = " + stampToDate(syscTime) +  "  score = " + face_score + "  areaName = " + areaName );
						//  fw_facerecord.write(userName+ "," + employeeNumber +"," + syscTime + "," + face_score + ","  + areaName + "\r\n");
						
						
			//			  if((syscTime.substring(0,10)).equals(margin_date))
			//				{
			//					break;
			//				}
			//			
						
						//////////////////////////write to file//////////////////
						
						// if(  (_score > 85 ) 
								{	 
						
						
						fw_facerecord.write(userName+ "," + employee_number +"," + stampToDate(syscTime) + "," + face_score + ","  + areaName + "\r\n");
						///////////////////////////
			//			FilePicLib = "LIB_" + userName + "_" + employeeNumber + "_" + dateToStamp(syscTime) + ".jpg";
			//			 FilePicCap = "CAP_" + userName + "_" + employeeNumber + "_" + dateToStamp(syscTime) + ".jpg";			                
			//			//   System.out.println("fileName = " + FilePicLib);
			//			DownloadPic.downLoadFromUrl(imageUri, FilePicLib, "c:\\pic");
			//			DownloadPic.downLoadFromUrl(visibleLightPic, FilePicCap, "c:\\pic");
						///////////////
						}
			 ////////////////////
					
				//
				//
				} // end of jo array
						
			 }			
				//
				//
				fw_facerecord.close();
			   }
///////////////////////////////////////////////////////////////

    public static String getBasic() {
        return basic;
    }



    public static void userAccessRecordsDump2Excel()  throws Exception {
				
				
				
				
			//	EntityResponse recordResponse = getRecord(token, 3, 2, 10);   
		        
			  
			    
			Integer index= 0;
			String employee_number;
			JSONObject jo_input = new JSONObject();
			JSONObject jo_object = new JSONObject();
			JSONObject jo_personInfo = new JSONObject();
			JSONObject jo_deviceinfo = new JSONObject();
			
			JSONObject jo_API_start= new JSONObject();;
			float  f_score;
			FileWriter fw_facerecord;
			fw_facerecord = new FileWriter("C:\\temp\\aaa.txt", true); 
//			FileWriter fw_WestGate;
//			FileWriter fw_SouthGate;
//			FileWriter fw_SouthWestGate;
			
//			String FilePicLib;
//			String FilePicCap;
			String format = "yyyy-MM-dd HH:mm:ss";
			
			int hour;
			
//			//jo_input.put("from", 0);
//			//jo_input.put("size", 10);
//			
//			//jo.put("startTime", "2018-03-16 12:00:05");
//			//jo.put("endTime", "2018-03-16 12:10:05");
//			//jo.put("areaId", "f42qe2r56qertgtasdfaqw45r");
//			//String target_date= "2018-10-31";
//			String target_date= "2018-10-30";
//			String  margin_date = "2018-10-31";
//			jo_input.put("startTime", "2018-10-30 00:00:01");
//			//jo_input.put("orderby", "desc");
//			jo_input.put("orderby", "asc");
//			//jo.put("personId", "pid");*/
//			//System.out.println(fr.userAccessRecords(jo_input));
//			jo_API_start = fr.userAccessRecords(jo_input);
			// System.out.println("record =====> " + JSONObject.toJSONString(recordResponse));
			
			
						
						
						
						//	fw_WestGate = new FileWriter("C:\\facerecord\\westgate01NOV.txt", true); 
						//fw_SouthWestGate = new FileWriter("C:\\facerecord\\southwestgate01NOV.txt", true); 
						//fw_SouthGate = new FileWriter("C:\\temp\\southgate30OCT.txt", true); 
						
			
			 for (index =0; index < 60; index++) {			
						
				 EntityResponse recordResponse = getRecord(token, 3, index, 10);
				  System.out.println("record =====> " + JSONObject.toJSONString(recordResponse));
				  System.out.println("entity =====> " + JSONObject.toJSONString(recordResponse.getEntity()));
					
					jo_API_start = JSONObject.parseObject((recordResponse.getEntity()).toString());
					System.out.println("jo_API_start =====> " +  jo_API_start.toJSONString());
										
					String qty = jo_API_start.getString("numberOfElements");
					
					//jo_object = jo_API_start.getJSONObject("content");
									
					System.out.println("qty =====> " + qty);
					
					
					// System.out.println("joarry =====> content" +  jo_API_start.getJSONArray("content"));
					
					System.out.println("joarry =====> content" +  jo_API_start.getJSONArray("content"));
					
					////////////////////////parse parameters///////////////////////////////
					JSONArray jsArry = new JSONArray();
					jsArry = jo_API_start.getJSONArray("content");
					JSONObject jo_array = new JSONObject();
					System.out.println("size of content = " + jsArry.size());
				  					
						for(int i= 0; i< jsArry.size(); i++) {
						
						
						//	jo_array = jsArry.getJSONObject(0);	
						jo_array = jsArry.getJSONObject(i);	
						String userName = jo_array.getString("name"); 
						String syscTime = jo_array.getString("syncTime"); 
						jo_personInfo = jo_array.getJSONObject("personInfo");
						jo_deviceinfo = jo_array.getJSONObject("deviceInfo");
						
						if(jo_personInfo != null && !jo_personInfo.isEmpty() )
						{
							 employee_number = jo_personInfo.getString("no");	
						}
						else {
							 employee_number = jo_array.getString("idNumber"); 
						}
						
						
						String face_score = jo_array.getString("score"); // must be greater than 0.63
						
						
						String areaName = jo_deviceinfo.getString("name"); 
						//String imageUri = jo_array.getString("imageUri");
						//String visibleLightPic = jo_array.getString("visibleLightPic");
						
						f_score = Float.valueOf(face_score);
						
						//System.out.println("imageUri = " + imageUri);		
						//	System.out.println("visibleLightPic = " + visibleLightPic);		
						
					//	System.out.println("userName = "+  userName  + "   employeeNumber = " + employee_number +"  syscTime = " + stampToDate(syscTime) +  "  score = " + face_score + "  areaName = " + areaName );
						//  fw_facerecord.write(userName+ "," + employeeNumber +"," + syscTime + "," + face_score + ","  + areaName + "\r\n");
						
						
			//			  if((syscTime.substring(0,10)).equals(margin_date))
			//				{
			//					break;
			//				}
			//			
						
						//////////////////////////write to file//////////////////
						
						if(f_score > 0.85) {	 
						
						fw_facerecord.write(userName+ "," + employee_number +"," + stampToDate(syscTime) + "," + face_score + ","  + areaName + "\r\n");
						///////////////////////////
			//			FilePicLib = "LIB_" + userName + "_" + employeeNumber + "_" + dateToStamp(syscTime) + ".jpg";
			//			 FilePicCap = "CAP_" + userName + "_" + employeeNumber + "_" + dateToStamp(syscTime) + ".jpg";			                
			//			//   System.out.println("fileName = " + FilePicLib);
			//			DownloadPic.downLoadFromUrl(imageUri, FilePicLib, "c:\\pic");
			//			DownloadPic.downLoadFromUrl(visibleLightPic, FilePicCap, "c:\\pic");
						///////////////
						}
			 ////////////////////
					
				//
				//
				} // end of jo array
						
			 }			
				//
				//
				fw_facerecord.close();
			   }
			
			
			 

//////////////////////////////////////////////////
			public static String stampToDate(String s){
		        String res;
		        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       // SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		        long lt = new Long(s);
		        Date date = new Date(lt);
		       // res = simpleDateFormat.format(date);
		        res = simpleDateFormat.format(date);
		        return res;
		    }  
    
    /////////////////////////////////////
}
