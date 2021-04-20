import com.google.gson.Gson;
import entities.Category;
import entities.Pet;
import entities.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiTests {
    String BASE_URL = "https://petstore.swagger.io/v2";
//    String[] namePet = {"Пират", "Боливар", "Кристо", "Майк", "Джек", "Тонни", "Люци", "Грин", "Люк"};
//    int index = (int) (Math.random() * namePet.length);


    @Test
    public void addNewPetToTheStore() {
        Category catCategory = new Category(131231231, "Cats");

        System.out.println("I preparing test data...");
        Pet petToAdd = Pet.builder()
                .id(new Random().nextInt(9))
                .category(catCategory)
                .name("Vasya")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("lab")
                .build();
        System.out.println("Body to send: " + new Gson().toJson(petToAdd));

        Response addingPetResponse = given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(petToAdd)
                .when()
                .post();

        System.out.println("Response : " + addingPetResponse.asString());

        Pet addedPetResponse = addingPetResponse.as(Pet.class);
        System.out.println("Что-то" + addedPetResponse);


        long ID = petToAdd.getId();
        Response getInfoAboutPet = given()
                .baseUri(BASE_URL)
                .pathParam("petId", ID)
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/{petId}");

        System.out.println("Response: " + getInfoAboutPet.asString());

        Pet gettingInfoAboutPet = getInfoAboutPet.as(Pet.class);

        String expectedName = petToAdd.getName();
        String actualName = gettingInfoAboutPet.getName();
        Assert.assertEquals("Wrong name", expectedName, actualName);


    }
    @Test

     public void createNewUser(){
        User userCreate = User.builder()
                .id(new Random().nextInt(3))
                .username("Vasa Pupkin")
                .firstName("Vasa")
                .lastName("Pupkin")
                .email("Vasa@Pupkin.com")
                .password("Love123")
                .phone("iphone")
                .userStatus(200)
                .build();
        System.out.println("Body to send createNewUser: " + new Gson().toJson(userCreate));

        Response addUserResponse = given()
                .baseUri(BASE_URL)
                .basePath("/user")
                .contentType(ContentType.JSON)
                .body(userCreate)
                .when()
                .post();

        System.out.println("Response addUser: " + addUserResponse.asString());

        User CreateNewUser = addUserResponse.as(User.class);
        System.out.println(CreateNewUser);

        Assert.assertEquals("Status not 200", 200, addUserResponse.getStatusCode());


        Response getInfoAboutUser = given()
                .baseUri(BASE_URL)
                .pathParam("username", "Vasa Pupkin")
                .when()
                .get("/user/{username}");

        System.out.println("Response get info about user: " + getInfoAboutUser.asString());

        User gettUserResponseName = getInfoAboutUser.as(User.class);

        Assert.assertEquals("Wrong name", userCreate.getEmail(), gettUserResponseName.getEmail());
        System.out.println(userCreate.getEmail());
        System.out.println(gettUserResponseName.getEmail());


    }



    }



































//    @Test
//    public void changeInfoAboutPet() {
//        Category catCategory = new Category(131231231, "Cats");
//
//        System.out.println("I preparing test data...");
//        Pet petToAdd = Pet.builder()
//                .id(new Random().nextInt(3))
//                .category(catCategory)
//                .name("Jozzy")
//                .photoUrls(Collections.singletonList("urls"))
//                .tags(null)
//                .status("available")
//                .build();
//        System.out.println("Body to send: " + new Gson().toJson(petToAdd));
//
//        Response addingPetResponse = given()
//                .baseUri(BASE_URL)
//                .basePath("/pet")
//                .contentType(ContentType.JSON)
//                .body(petToAdd)
//                .when()
//                .post();
//
//        System.out.println("Response: " + addingPetResponse.asString());
//
//        Pet addedPetResponse = addingPetResponse.as(Pet.class);
//        long petId = addedPetResponse.getId();
//
//        petToAdd.setName("Qwerty");
//        Pet changedPet = petToAdd;
//
//        Response changePetIdResponse = given()
//                .baseUri(BASE_URL)
//                .basePath("/pet")
//                .contentType(ContentType.JSON)
//                .body(changedPet)
//                .when()
//                .put();
//
//        Pet changedPetFromResponse = changePetIdResponse.as(Pet.class);
//
//        Assert.assertEquals("Wrong new name of pet", petToAdd.getName(), changedPetFromResponse.getName());
//    }
//}
//@Test