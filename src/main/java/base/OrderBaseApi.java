package base;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import java.util.List;
import java.util.Random;

public class OrderBaseApi extends RestClient{
    private static final String Order_URI = RestClient.BASE_URI + "orders";
    private List<String> ingredientsList;
    private Response orderResponse;
    @Step("Получение списка ингридиентов")
    public void setIngredientsList(){
        ingredientsList = given()
            .spec(getReqSpec())
            .get(Order_URI)
            .then()
            .extract()
            .path("data._id");
    }

    @Step("Создание заказа без авторизации")
    public void createOrderAuth(){
        Random random = new Random();
        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
    orderResponse = given()
            .spec(getReqSpec())
            .body(randomIngredientFromList)
            .when()
            .post(Order_URI);
    }
}
