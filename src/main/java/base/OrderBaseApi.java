package base;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import java.util.List;
import java.util.Random;

public class OrderBaseApi extends RestClient{
    private static final String Order_URI = RestClient.BASE_URI + "orders";
    private static final String Ingredients_URI = RestClient.BASE_URI + "ingredients";
    private List<String> ingredientsList;
    private Response orderResponse;
    private String accessToken;

    @Step("Извлечение тела ответа")
    public Response getOrderResponse() {
        return orderResponse;
    }
    @Step("Получение списка ингридиентов")
    public void setIngredientsList(){
        ingredientsList = given()
            .spec(getReqSpec())
            .get(Ingredients_URI)
            .then()
            .extract()
            .path("data._id");
    }
    @Step("Создание заказа с токеном авторизации")
    public void createOrderAuth(String accessToken) {
        Random random = new Random();
        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
                 given()
                .spec(getReqSpec())
                .headers("authorization", accessToken)
                .body(randomIngredientFromList)
                .when()
                .post("orders");

    }
//    @Step("Создание заказа без авторизации")
//    public void createOrderAuth(){
//        Random random = new Random();
//        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
//    orderResponse = given()
//            .spec(getReqSpec())
//            .header("Authorization",accessToken)
//            .body(randomIngredientFromList)
//            .when()
//            .post(Order_URI);
//    }
    @Step("Создание заказа без ингридиентов")
    public void createOrderNoIngredient(String accessToken){
        orderResponse = given()
                .spec(getReqSpec())
                .headers("Authorization", accessToken)
                .when()
                .post(Order_URI);
    }
    @Step("Создание заказа с неверным хэшэм ингредиентов")
    public void createOrderWithInvalidIngredientHash(String accessToken) {
        Random random = new Random();
        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
        orderResponse = given()
                .spec(getReqSpec())
                .headers("Authorization", accessToken)
                .body(randomIngredientFromList)
                .when()
                .post(Order_URI);
    }
    @Step("Получение заказов конкретного авторизированного пользователя")
    public void getOrderAuth(String accessToken) {
        orderResponse = given()
                .spec(getReqSpec())
                .headers("Authorization", accessToken)
                .when()
                .get("orders");
    }
    @Step("Получение заказов неавторизованного пользователя")
    public void getOrderUnauth() {
        orderResponse = given()
                .spec(getReqSpec())
                .when()
                .get("orders");
    }
}
