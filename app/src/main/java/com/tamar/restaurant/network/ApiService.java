package com.tamar.restaurant.network;

import com.tamar.restaurant.models.CallType;
import com.tamar.restaurant.models.Dish;
import com.tamar.restaurant.models.OrderItem;
import com.tamar.restaurant.models.OrderStatus;
import com.tamar.restaurant.models.Restaurant;
import com.tamar.restaurant.models.Order;
import com.tamar.restaurant.models.user.User;
import com.tamar.restaurant.models.WaiterCall;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants(@Header("Authorization") String token);

    @POST("orders/add")
    Call<Order> createOrder(
            @Header("Authorization") String token,
            @Query("table_id") Long tableId,
            @Query("user_id") Long userId
    );

    @POST("users/google")
    Call<User> loginWithGoogle(@Header("Authorization") String idToken);


    @GET("dishes/get_dishes_by_table")
    Call<List<Dish>> getDishesByTable(@Header("Authorization") String idToken, @Query("table_id") Long tableId);

    @POST("orderItems/{orderId}/{dishId}")
    Call<OrderItem> addOrderItem(@Header("Authorization") String token, @Path("orderId") Long orderId, @Path("dishId") Long dishId);


    @GET("users/userinfo")
    Call<User> getUserInfo(@Header("Authorization") String idToken);

    @GET("restaurants/get_restaurant_from_table")
    Call<Restaurant> getRestaurantFromTableId(
            @Header("Authorization") String idToken,
            @Query("table_id") Long tableId
    );

    @POST("waiter-calls/call")
    Call<WaiterCall> createCall(@Header("Authorization") String idToken,
                                @Query("assignment_id") Long assignmentId,
                                @Query("call_type") CallType callType);

//    @GET("waiters/{restaurantId}")
//    Call<List<Waiter>> getWaitersInRestaurant(@Header("Authorization") String idToken,@Query("restaurantId") Long restaurantId);

    @GET("orderItems/order/{restaurantId}")
    Call<List<OrderItem>> getOrderItemsByRestaurant(@Header("Authorization") String idToken,
                                                    @Path("restaurantId") Long restaurantId);
    @GET("orderItems/order/{orderId}")
    Call<List<OrderItem>> getOrderItemsByOrder(@Header("Authorization") String idToken,
                                               @Path("orderId") Long orderId);

    @GET("orderItems/restaurant")
    Call<List<OrderItem>> getItemsByStatusAndRestaurant(
            @Header("Authorization") String token,
            @Query("restaurantId") Long restaurantId,
            @Query("status") List<OrderStatus> statusList
    );

    @PUT("orderItems/{orderItemId}/status/{status}")
    Call<OrderItem> updateOrderItemStatus(@Header("Authorization") String idToken,
            @Path("orderItemId") Long orderItemId, @Path("status") OrderStatus status);
    @GET("restaurants/get_restaurant_from_kitchen")
    Call<Restaurant> getRestaurantFromKitchenId(@Header("Authorization") String idToken,@Query("kitchen_id") Long kitchen_id );
}

