package com.zk.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 这是一个示例：
 * 使用jackson实现json和java对象的互相转化
 * @author zhouKai
 * @createDate 2016年11月16日 下午1:32:37
 */
public class JacksonExample {

	private static final int Birthday = 0;
	private JsonGenerator jsonGenerator = null;
	private ObjectMapper objectMapper = null;
	private Account bean = null;
	private int List;

	@Before
	public void init() {
		bean = new Account();
		bean.setId(17);
		bean.setName("zhouKai");
		bean.setAddress("huaiBei");
		bean.setAddress("2439786299@qq.com");

		objectMapper = new ObjectMapper();
		try {
			jsonGenerator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void destory() {
		try {
			if (jsonGenerator != null) {
				jsonGenerator.flush();
			}
			if (!jsonGenerator.isClosed()) {
				jsonGenerator.close();
			}
			jsonGenerator = null;
			objectMapper = null;
			bean = null;
			Runtime.getRuntime().gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>将java对象转换成json字符串
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:01:10
	 */
	@Test
	public void writeEntityJSON() {
		try {
			System.out.println("jsonGenerator");
			// writeObject可以转换java对象，eg:JavaBean/Map/List/Array等
			jsonGenerator.writeObject(bean);
			System.out.println();

			System.out.println("ObjectMapper");
			// writeValue具有和writeObject相同的功能
			objectMapper.writeValue(System.out, bean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>将map转换成json字符串
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:05:26
	 */
	@Test
	public void writeMapJSON() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", bean.getName());
			map.put("account", bean);

			bean = new Account();
			bean.setAddress("china-Beijin");
			bean.setEmail("hoojo@qq.com");
			map.put("account2", bean);

			System.out.println("jsonGenerator");
			jsonGenerator.writeObject(map);
			System.out.println("");

			System.out.println("objectMapper");
			objectMapper.writeValue(System.out, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>将list集合转换成json字符串
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:05:59
	 */
	@Test
	public void writeListJSON() {
		try {
			List<Account> list = new ArrayList<Account>();
			list.add(bean);

			bean = new Account();
			bean.setId(2);
			bean.setAddress("address2");
			bean.setEmail("email2");
			bean.setName("haha2");
			list.add(bean);

			System.out.println("jsonGenerator");
			// list转换成JSON字符串
			jsonGenerator.writeObject(list);
			System.out.println();
			System.out.println("ObjectMapper");
			// 用objectMapper直接返回list转换成的JSON字符串
			System.out.println("1###" + objectMapper.writeValueAsString(list));
			System.out.print("2###");
			// objectMapper list转换成JSON字符串
			objectMapper.writeValue(System.out, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * jackson提供的一些类型，用这些类型完成json转换；
	 * 如果你使用这些类型转换JSON的话，那么你即使没有JavaBean(Entity)也可以完成复杂的Java类型到JSON的转换。
	 * 下面用到这些类型构建一个复杂的Java对象，并完成JSON转换。
	 */
	@Test
	public void writeOthersJSON() {
		try {
			String[] arr = { "a", "b", "c" };
			System.out.println("jsonGenerator");
			String str = "hello world jackson!";
			// byte
			jsonGenerator.writeBinary(str.getBytes());
			// boolean
			jsonGenerator.writeBoolean(true);
			// null
			jsonGenerator.writeNull();
			// float
			jsonGenerator.writeNumber(2.2f);
			// char
			jsonGenerator.writeRaw("c");
			// String
			jsonGenerator.writeRaw(str, 5, 10);
			// String
			jsonGenerator.writeRawValue(str, 5, 5);
			// String
			jsonGenerator.writeString(str);
			//			jsonGenerator.writeTree(JsonNodeFactory.instance.POJONode(str));
			System.out.println();

			// Object
			jsonGenerator.writeStartObject();// {

			jsonGenerator.writeObjectFieldStart("user");// user:{
			jsonGenerator.writeStringField("name", "jackson");// name:jackson
			jsonGenerator.writeBooleanField("sex", true);// sex:true
			jsonGenerator.writeNumberField("age", 22);// age:22
			jsonGenerator.writeEndObject();// }

			jsonGenerator.writeArrayFieldStart("infos");// infos:[
			jsonGenerator.writeNumber(22);// 22
			jsonGenerator.writeString("this is array");// this is array
			jsonGenerator.writeEndArray();// ]

			jsonGenerator.writeEndObject();// }

			Account bean = new Account();
			bean.setAddress("address");
			bean.setEmail("email");
			bean.setId(1);
			bean.setName("haha");

			// complex Object
			System.out.println();
			jsonGenerator.writeStartObject();// {
			jsonGenerator.writeObjectField("user", bean);// user:{bean}
			jsonGenerator.writeObjectField("infos", arr);// infos:[array]
			jsonGenerator.writeEndObject();// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * JSON转换成Java对象
	 */
	@Test
	public void readJson2Entity() {
		String json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
		Account acc;
		try {
			acc = objectMapper.readValue(json, Account.class);
			System.out.println(acc.getName());
			System.out.println(acc);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>json字符串转换成list<map>
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:12:01
	 */
	@Test
	public void readJson2List() {
		String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
		System.out.println(json);
		System.out.println();
		try {
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				Set<String> set = map.keySet();
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String key = it.next();
					System.out.println(key + ":" + map.get(key));
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>json字符串转换成Array
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:14:01
	 */
	@Test
	public void readJson2Array() {
		String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
		try {
			Account[] arr = objectMapper.readValue(json, Account[].class);
			List<Account> list = Arrays.asList(arr);
			for (Iterator<Account> it = list.iterator(); it.hasNext();) {
				Account accountBean = (Account) it.next();
				System.out.println(accountBean);
			}
			System.out.println(arr.length);
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i]);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>json字符串转换Map集合
	 * @author hoojo
	 * @createDate Nov 27, 2010 3:00:06 PM
	 */
	@Test
	public void readJson2Map() {
		String json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"
				+ "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
		System.out.println(json);
		try {
			@SuppressWarnings(value = { "unchecked" })
			Map<String, Map<String, Object>> maps = objectMapper.readValue(json, Map.class);
			System.out.println(maps.size());
			Set<String> key = maps.keySet();
			Iterator<String> iter = key.iterator();
			while (iter.hasNext()) {
				String field = iter.next();
				System.out.println(field + ":" + maps.get(field));
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>function:</b>java对象转换成xml文档
	 * 需要额外的jar包 stax2-api.jar
	 * @author hoojo
	 * @createDate 2010-11-23 下午06:11:21
	 */
	@Test
	public void writeObject2Xml() {
		//stax2-api-3.0.2.jar
		System.out.println("XmlMapper");
		/*
		XmlMapper xml = new XmlMapper();
		
		try {
		    //javaBean转换成xml
		    //xml.writeValue(System.out, bean);
		    StringWriter sw = new StringWriter();
		    xml.writeValue(sw, bean);
		    System.out.println(sw.toString());
		    //List转换成xml
		    List<AccountBean> list = new ArrayList<AccountBean>();
		    list.add(bean);
		    list.add(bean);
		    System.out.println(xml.writeValueAsString(list));
		    
		    //Map转换xml文档
		    Map<String, AccountBean> map = new HashMap<String, AccountBean>();
		    map.put("A", bean);
		    map.put("B", bean);
		    System.out.println(xml.writeValueAsString(map));
		} catch (JsonGenerationException e) {
		    e.printStackTrace();
		} catch (JsonMappingException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}*/
	}

	@Test
	public void test00() throws Exception {
		try {
		Map<String, Account> map = new HashMap<>();
		map.put("aa", new Account(1, "aa", new Birthday("2011")));
		map.put("bb", new Account(2, "bb", new Birthday("2022")));
		String str = objectMapper.writeValueAsString(map);
		System.out.println(str);
		Map aa = objectMapper.readValue(str, Map.class);
		System.out.println(aa);
		System.out.println(aa.getClass());
		
			JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(HashMap.class, HashMap.class, String.class, Account.class);
			Map<String, Account> lst = objectMapper.readValue(str, javaType);
			System.out.println(lst);
			System.out.println(lst.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test01() throws Exception {
		List<Map<String, Account>> ls = new ArrayList<>();
		Map<String, Account> p1 = new HashMap<>();
		p1.put("aa", new Account(1, "aa", new Birthday("2011")));
		p1.put("bb", new Account(2, "bb", new Birthday("2022")));
		ls.add(p1);
		Map<String, Account> p2 = new HashMap<>();
		p2.put("cc", new Account(3, "cc", new Birthday("2033")));
		p2.put("dd", new Account(4, "dd", new Birthday("2044")));
		ls.add(p2);
		
		String str = objectMapper.writeValueAsString(ls);
		System.out.println(str);
		JavaType childType = objectMapper.getTypeFactory()
				.constructParametrizedType(HashMap.class, Map.class, String.class, Account.class);;
		JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, childType);
		List<Map<String, Object>> lst = objectMapper.readValue(str, javaType);
		System.out.println(lst);
	}
	
	@Test
	public void test02() {
		try {
//			ProcessResult<Person> home = new ProcessResult<Person>();
//			home.setData(new Person("aa", 11));
//			home.setCnt(100);
//			String json = objectMapper.writeValueAsString(home);
//			System.out.println(json);
//			
//			JavaType type = objectMapper.getTypeFactory().constructParametrizedType(Home.class, Home.class, Person.class);
//			Home aa = objectMapper.readValue(json, type);
//			System.out.println(aa);
//			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

}

class Person {
	private String name;
	private int age;

	public Person() {
		// TODO Auto-generated constructor stub
	}
	
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
}


class Home<T> {
	
	private T data;
	
	private int cnt;
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}

	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
	@Override
	public String toString() {
		return "Home [data=" + data + ", cnt=" + cnt + "]";
	}
	
	
}
