import com.github.fge.jsonschema.main.JsonSchema;
import com.google.gson.Gson;
import entities.Category;
import entities.Pet;
import entities.SuccessfulResponse;
import entities.User;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ApiTests {
    String BASE_URL = "https://petstore.swagger.io/v2";
//    String[] namePet = {"Пират", "Боливар", "Кристо", "Майк", "Джек", "Тонни", "Люци", "Грин", "Люк"};
//    int index = (int) (Math.random() * namePet.length);


    @Test
    public void addNewPetToTheStore() throws InterruptedException {
        Category catCategory = new Category(131231231, "Cats");

        System.out.println("I preparing test data...");
        Pet petToAdd = Pet.builder()
                .id(BigInteger.valueOf(new Random().nextInt(9)))
                .category(catCategory)
                .name("Vadim")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("sold")
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
        System.out.println("переменная в которой сохранен респонс" + addedPetResponse);

        TimeUnit.SECONDS.sleep(4);
       // String statusPet = petToAdd.getStatus();
        System.out.println("статус который мы получили из тела"+"  " + petToAdd.getStatus());
        Response getStatusAboutPet = given()
                .baseUri(BASE_URL)
               // .pathParam("status", statusPet)
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/findByStatus/?status=sold");

        System.out.println("ResponseGetByStatus: " + getStatusAboutPet.asString());

      //  Pet[] gettingInfoAboutPet = getStatusAboutPet.as(Pet[].class);
        TimeUnit.SECONDS.sleep(5);

        List<Pet> petsSold = Arrays.stream(getStatusAboutPet.as(Pet[].class))
                .filter(pet -> pet.getId().equals(petToAdd.getId()))
                .collect(Collectors.toList());

        Assert.assertEquals("Name is not needed", petToAdd.getName(), petsSold.get(0).getName());
        Assert.assertEquals("There is no pet with such id", 1, petsSold.size());




    }
    @Test
    public void EmptyID() {

        int idEmpty = 0;

        for (int i = 1; i <= 100; i++) {
            int save = given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/pet/" + i)
                    .then().extract().statusCode();

            if (save != 200) {
                idEmpty++;
            }
        }
        System.out.println(idEmpty);
    }
    @Test
    public void addPetNotValidId() throws InterruptedException {
        Category catCategory = new Category(123456, "Cats");

        System.out.println("I preparing test data...");
        BigInteger Big = new BigInteger( "333315351352132135165");
        Pet petToAddNotValid = Pet.builder()
                .id (Big)
                .category(catCategory)
                .name("Sveta")
                .photoUrls(Collections.singletonList("urls"))
                .tags(null)
                .status("lab")
                .build();
        System.out.println("Body to send: " + new Gson().toJson(petToAddNotValid));

        Response addingPetResponseNotValid = given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(petToAddNotValid)
                .when()
                .post();

        System.out.println("Response : " + addingPetResponseNotValid.asString());

        TimeUnit.SECONDS.sleep(4);
        Assert.assertEquals("Status valid", 500,addingPetResponseNotValid.getStatusCode());
        System.out.println(addingPetResponseNotValid.getStatusLine());
        SuccessfulResponse notValid = addingPetResponseNotValid.as(SuccessfulResponse.class);
        Assert.assertEquals("message is wrong", "something bad happened",notValid.getMessage());
        TimeUnit.SECONDS.sleep(4);
    }

    @Test

     public void createNewUser() throws InterruptedException {
        User userCreate = User.builder()
                .id(9)
                .username("VasaPupkin")
                .firstName("Vasa")
                .lastName("Pupkin")
                .email("Vasa@email.com")
                .password("Love123")
                .phone("8093123456789")
                .userStatus(200)
                .build();
        System.out.println("Body to send createNewUser: " + new Gson().toJson(userCreate));

        Response addUserResponseDelete = given()
                .baseUri(BASE_URL)
                .basePath("/user")
                .contentType(ContentType.JSON)
                .body(userCreate)
                .when()
                .post();

        System.out.println("Response addUser: " + addUserResponseDelete.asString());

        SuccessfulResponse createNewUserResponse = addUserResponseDelete.as(SuccessfulResponse.class);
        System.out.println(createNewUserResponse);

        Assert.assertEquals("Status not 200", 200, createNewUserResponse.getCode());

        Response getInfoAboutUser = given()
                .baseUri(BASE_URL)
                .pathParam("username", "VasaPupkin")
                .when()
                .get("/user/{username}")
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserSchema.json"))
                .extract()
                .response();
        TimeUnit.SECONDS.sleep(4);
        System.out.println("Response get info about user: " + getInfoAboutUser.asString());

          User getInfoAboutUserSchema = getInfoAboutUser.as(User.class);
        System.out.println("переменная к которой приведен респонс гета" + getInfoAboutUserSchema);



    }
    @Test
    public void addNewPetToTheStoreforDelete() throws InterruptedException {
        Category catCategory = new Category(131231231, "Cats");

        System.out.println("I preparing test data для удаления...");
        Pet petToAddd = Pet.builder()
                .id(BigInteger.valueOf(new Random().nextInt(9)))
                .category(catCategory)
                .name("VadimDelete")
                .photoUrls(Collections.singletonList("urlsDelete"))
                .tags(null)
                .status("labDelete")
                .build();
        System.out.println("Body to send: " + new Gson().toJson(petToAddd));

        Response addingPetResponsed = given()
                .baseUri(BASE_URL)
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(petToAddd)
                .when()
                .post();

        System.out.println("Response : " + addingPetResponsed.asString());
        Pet addedPetResponsed = addingPetResponsed.as(Pet.class);
        System.out.println("переменная в которой сохранен респонс" + addedPetResponsed);
        Assert.assertEquals("Status not 200", 200, addingPetResponsed.getStatusCode());
        System.out.println(addedPetResponsed.getStatus());


        TimeUnit.SECONDS.sleep(4);

                BigInteger ID = petToAddd.getId();
        Response deleteMyPet = given()
                .baseUri(BASE_URL)
                .pathParam("petId", ID)
                .contentType(ContentType.JSON)
                .when()
                .delete("/pet/{petId}");

        Assert.assertEquals("Status not 200",200, deleteMyPet.getStatusCode());
        System.out.println(" my status after delete"+ deleteMyPet.getStatusCode());

        TimeUnit.SECONDS.sleep(4);


        Response CheckGetAfterDelete = given()
                .baseUri(BASE_URL)
                .pathParam("petId", ID)
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/{petId}");
        TimeUnit.SECONDS.sleep(4);

        Assert.assertEquals("User is found",404,CheckGetAfterDelete.getStatusCode());






//
//        System.out.println("Response: " + getInfoAboutPet.asString());
//
//        Pet gettingInfoAboutPet = getInfoAboutPet.as(Pet.class);
//
//        String expectedName = petToAdd.getName();
//        String actualName = gettingInfoAboutPet.getName();
//        Assert.assertEquals("Wrong name", expectedName, actualName);
//
//


    }}



































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