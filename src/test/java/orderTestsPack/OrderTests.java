package orderTestsPack;
import base.OrderBaseApi;
import base.UserBaseApi;
import base.util.GeneratorData;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
public class OrderTests {
    private String accessToken;
    private UserBaseApi userBaseApi;
    private OrderBaseApi orderBaseApi;
    private User user;


    @Before
    public void setUp(){
        orderBaseApi = new OrderBaseApi();
        userBaseApi = new UserBaseApi();
        user = GeneratorData.createUser();
        orderBaseApi.setIngredientsList();
    }
    @After
    public void tearDown(){
        userBaseApi.delete();
    }

    @Tag("Доработать")
    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void checkOrderWithAuth(){
        ValidatableResponse response = userBaseApi.registrateUser(user);
        accessToken = response.extract().path("accessToken").toString();
        userBaseApi.setAccessToken(accessToken);
        orderBaseApi.createOrderAuth(userBaseApi.getAccessToken());
       // orderBaseApi.getOrderAuth(accessToken);
//        orderBaseApi.getOrderResponse()
//                .then()
//                .statusCode(SC_OK)
//                .and()
//                .body("success",equalTo(true));

    }
}
