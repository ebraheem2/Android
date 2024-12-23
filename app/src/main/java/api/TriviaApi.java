package api;

import model.TriviaResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaApi {
    @GET("api.php")
    Call<TriviaResponse> getQuestions(
       @Query("amount")int amount,
       @Query("category")int catagory,
       @Query("type")String type
    );
}
