package Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class Validation {

	
	@Test
	public void checkDateRange() {
		JsonPath js = new JsonPath(payload.WeatherData());
		int count = js.getInt("list.size()");
		int initialDate = Integer.parseInt(js.get("list[0].dt_txt").toString().substring(8,10));
		String check = "true";
		
		for(int i=0;i<count;i++) {
			int date = Integer.parseInt(js.get("list["+i+"].dt_txt").toString().substring(8,10));
			System.out.println(date);
			if(date<initialDate||date>(initialDate+5)) {
			
				check="false";
				break;
			}
		}
		Assert.assertEquals(check,"true");
	}
	
	
	@Test
	public void checkDescription() {
		JsonPath js = new JsonPath(payload.WeatherData());
		int count = js.getInt("list.size()");
		for(int i=0;i<count;i++) {
			int id = js.getInt("list["+i+"].weather[0].id");
			String description = js.get("list["+i+"].weather[0].description");
			String main = js.get("list["+i+"].weather[0].main");
			String icon = js.get("list["+i+"].weather[0].icon");
			if(id==500) {
				
				Assert.assertEquals(description, "light rain");
				
				System.out.println("id:"+id);
				System.out.println("main:"+main);
				System.out.println("description:"+description);
				System.out.println("icon:"+icon);
			}
			else if(id==800) {
				Assert.assertEquals(description, "clear sky");
				System.out.println("dd:"+id);
				System.out.println("main:"+main);
				System.out.println("description:"+description);
				System.out.println("icon:"+icon);
			}
		}
		
	}
	
	@Test
	public void checkTemperature() {
		JsonPath js = new JsonPath(payload.WeatherData());
		int count = js.getInt("list.size()");
		float temp,temp_min,temp_max;
		String condition="true";
		for(int i=0;i<count;i++) {
			 temp = js.getFloat("list["+i+"].main.temp");
			 temp_min = js.getFloat("list["+i+"].main.temp_min");
			 temp_max = js.getFloat("list["+i+"].main.temp_max");
			 if(temp<temp_min||temp>temp_max) {
				 condition="false";
				 break;
			 }
		}
		Assert.assertEquals(condition, "true");
	}
	
	@Test
	
	public void checkHourlyInterval() {
		JsonPath js = new JsonPath(payload.WeatherData());
		int count = js.getInt("list.size()");
		for(int i=1;i<count;i++)
		{
			
			String dateStart= js.get("list["+(i-1)+"].dt_txt");
			String dateStop= js.get("list["+i+"].dt_txt");
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss"); 
			
			 Date d1 = null;
		        Date d2 = null;
		        try {
		            d1 = format.parse(dateStart);
		            d2 = format.parse(dateStop);
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }    

		        // Get msec from each, and subtract.
		        long diff = d2.getTime() - d1.getTime();

		        long days = TimeUnit.MILLISECONDS.toDays(diff);
		        long remainingHoursInMillis = diff - TimeUnit.DAYS.toMillis(days);
		        long hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis);
		       
		        System.out.println(hours);
		        Assert.assertEquals(hours, 1);

		}
		
		
	}
}
