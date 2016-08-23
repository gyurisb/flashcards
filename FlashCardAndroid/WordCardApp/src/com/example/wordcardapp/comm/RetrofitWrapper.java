package com.example.wordcardapp.comm;

import java.io.IOException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Static class to access communication API from everywhere
 * @author Bence
 *
 */
public class RetrofitWrapper {

	public static String baseURL = "https://testweb-gyurisb.azurewebsites.net";
	
	public static String authenticationToken = null;
	
	private static AccountAPI accountAPI;
	public static AccountAPI getAccountAPI() {
		return accountAPI;
	}
	
	private static CardAPI cardAPI;
	public static CardAPI getCardAPI() {
		return cardAPI;
	}
	
	private static CategoryAPI categoryAPI;
	public static CategoryAPI getCategoryAPI() {
		return categoryAPI;
	}
	
	// Static init
	static {
		// Init retrofit for unauthorized requests
		Retrofit retrofit = new Retrofit.Builder()
		.baseUrl(baseURL)
		.addConverterFactory(GsonConverterFactory.create())
		//.addConverterFactory(SimpleXmlConverterFactory.create())
		.build();
		// Init API
		accountAPI = retrofit.create(AccountAPI.class);
				
		// Init retrofit for authorized requests
		// Create OkHttp interceptor to add Authorization header to all requests
		OkHttpClient httpClient = new OkHttpClient();
		httpClient.networkInterceptors().add(new Interceptor() {
		    @Override
		    public Response intercept(Chain chain) throws IOException {
		        Request request = chain.request().newBuilder()
		        		.addHeader("Authorization", "Bearer " + authenticationToken)
		        		.build();
		        return chain.proceed(request);
		    }
		});
		Retrofit authorizedRetrofit = new Retrofit.Builder()
		.baseUrl(baseURL)
		.client(httpClient)
		.addConverterFactory(GsonConverterFactory.create())
		//.addConverterFactory(SimpleXmlConverterFactory.create())
		.build();
		// Init APIs
		cardAPI = authorizedRetrofit.create(CardAPI.class);
		categoryAPI = authorizedRetrofit.create(CategoryAPI.class);
	}

}
