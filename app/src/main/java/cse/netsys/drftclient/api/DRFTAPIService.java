package cse.netsys.drftclient.api;

import cse.netsys.drftclient.model.LoginReq;
import cse.netsys.drftclient.model.LoginResp;
import cse.netsys.drftclient.model.SignupReq;
import cse.netsys.drftclient.model.SignupResp;
import cse.netsys.drftclient.model.Snippet;
import cse.netsys.drftclient.model.Snippets;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DRFTAPIService {
    @GET("/snippets")
    Call<Snippets> listSnippets();

    @POST("/snippets/")  // The trailing slash necessary in POST
    Call<Snippet> createSnippet(@Body Snippet snippet);

    @GET("/snippets/{id}")
    Call<Snippet> detailSnippet(@Path("id") int id);

    @PUT("/snippets/{id}/")  // The trailing slash necessary in PUT
    Call<Snippet> updateSnippet(@Path("id") int id, @Body Snippet snippet);

    @DELETE("/snippets/{id}/")  // The trailing slash necessary in DELETE
    Call<Void> deleteSnippet(@Path("id") int id);

    @GET("/snippets/{id}/highlight")
    Call<Snippet> highlightSnippet(@Path("id") int id);

    @POST("/authentication/signup/")
    Call<SignupResp> signup(@Body SignupReq signupReq);

    @POST("/authentication/login/")
    Call<LoginResp> login(@Body LoginReq loginReq);
}
