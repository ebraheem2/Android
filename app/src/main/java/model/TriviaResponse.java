package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TriviaResponse {
    @SerializedName("response_code")
    private int responseCode;
    @SerializedName("results")
    private List<Question> resutls;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List<Question> getResults() {
        return resutls;
    }

    public void setResutls(List<Question> resutls) {
        this.resutls = resutls;
    }
}
