package com.example.mysocialapp.ui;


import com.example.mysocialapp.domain.validators.ValidationException;
import com.example.mysocialapp.service.FriendshipsService;
import com.example.mysocialapp.service.MessageService;
import com.example.mysocialapp.service.RequestsService;
import com.example.mysocialapp.service.UserService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class UI {
    Scanner scanner = new Scanner(System.in);
    UserService userService;
    FriendshipsService friendshipsService;

    MessageService messageService;
    RequestsService requestsService;



    public UI(UserService userService, FriendshipsService friendshipsService, MessageService messageService, RequestsService requestsService){
        this.userService = userService;
        this.friendshipsService = friendshipsService;
        this.messageService = messageService;
        this.requestsService = requestsService;
    }

    public void menu(){
        String select = "0";
        while(!select.equals("x")){
            printMenu();
            select=scanner.nextLine();
            switch(select){
                case "1" -> addUser();
                case "2" -> deleteUser();
                case "3" -> updateUser();
                case "4" -> showUsers();
                case "5" -> addFriendship();
                case "6" -> deleteFriendship();
                case "7" -> showFriendship();
//                case "8" -> showNumberOfCommunities();
//                case "9" -> showBiggestCommunity();
                case "10" -> addMessage();
                case "11" -> deleteMessage();
                case "12" -> showMessage();
                case "13" -> showConversation();
//                case "14" -> showFriendshipsForAnUser();
//                case "15" -> showFriendshipsForAnUserFromAMonth();
                case "16" -> addFriendRequest();
                case "17" -> showAllFriendRequests();
                case "18" -> showAllFriendRequestsForAnUser();
                case "19" -> acceptOrRejectRequest();
                case "x" -> System.out.println("BYE BYE!");
                default -> System.out.println("The option is incorrect! Please try again");

            }
        }

    }

    private void printMenu(){
        System.out.println("1. Add an user");
        System.out.println("2. Delete an user");
        System.out.println("3. Update an user");
        System.out.println("4. Show all users");
        System.out.println("5. Add friendship");
        System.out.println("6. Delete friendship");
        System.out.println("7. Show all friendships");
        System.out.println("8. Number of communities");
        System.out.println("9. Biggest community");
        System.out.println("10. Add a message");
        System.out.println("11. Delete a message");
        System.out.println("12. Show all messages");
        System.out.println("13. Conversation between 2 users");
        System.out.println("14. Friends of an user");
        System.out.println("15. Friends of an user from a month");
        System.out.println("16. Add a friend request");
        System.out.println("17. Show all friend requests");
        System.out.println("18. Show all friend requests for a specific user");
        System.out.println("19. Accept or reject a friend request");
        System.out.println("x. Exit");
        System.out.println("Please select your option: ");
    }

    private void addUser(){
        System.out.println("Give the first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Give the last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        System.out.println("Gender: ");
        String gender = scanner.nextLine();
        try{
            this.userService.addUser(firstName,lastName,email,password,gender);
            System.out.println("User added successfully!");
        } catch (ValidationException | IllegalArgumentException error){
            System.out.println(error.getMessage());
        }

    }

    private void deleteUser(){
        System.out.println("ID you want to delete: ");
        Long id = scanner.nextLong();
        //this.friendshipService TO DO
        this.userService.deleteUser(id);
        System.out.println("USER DELETED SUCCESSFULLY");
        scanner.nextLine();
    }

    public void updateUser(){
        System.out.println("User ID: ");
        Long id = Long.valueOf(scanner.nextLine());
        System.out.println("New first name: ");
        String new_first_name = scanner.nextLine();
        System.out.println("New last name: ");
        String new_last_name = scanner.nextLine();
        System.out.println("New email: ");
        String new_email = scanner.nextLine();
        System.out.println("New password: ");
        String new_password = scanner.nextLine();
        System.out.println("New gender: ");
        String new_gender = scanner.nextLine();
        try{
            this.userService.updateUser(id, new_first_name,new_last_name,new_email,new_password,new_gender);
            System.out.println("USER UPDATED SUCCESSFULLY");
        }catch (ValidationException | IllegalArgumentException error){
            System.out.println(error.getMessage());
        }
    }

    private void addFriendship() {
        System.out.println("First user ID: ");
        Long id1 = Long.parseLong(scanner.next());
        System.out.println("Second user ID: ");
        Long id2 = Long.parseLong(scanner.next());
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            this.friendshipsService.addFriendship(id1, id2, currentTime);
            System.out.println("FRIENDSHIP ADDED SUCCESSFULLY");
        } catch (ValidationException | IllegalArgumentException error) {
            System.out.println(error.getMessage());
        }
        scanner.nextLine();
    }

    private void deleteFriendship() {
        System.out.println("ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        this.friendshipsService.deleteFriendship(id);
        System.out.println("FRIENDSHIP DELETED SUCCESSFULLY");
        scanner.nextLine();
    }

    private void showUsers(){
        this.userService.getAllUsers().forEach(System.out::println);
    }


    private void showFriendship() {
        this.friendshipsService.getAllFriendships().forEach(System.out::println);
    }

    private void addMessage() {
        System.out.println("From(id): ");
        Long from = Long.parseLong(scanner.nextLine());

        System.out.println("To(id): ");
        Long to = Long.parseLong(scanner.nextLine());

        System.out.println("Message: ");
        String messageText = scanner.nextLine();

        System.out.println("id replay: ");
        Long idReplay = Long.parseLong(scanner.nextLine());

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            this.messageService.addMessage(
                    from, to, messageText, currentTime, idReplay);
            System.out.println("MESSAGE ADDED SUCCESSFULLY");
        } catch (ValidationException | IllegalArgumentException error) {
            System.out.println(error.getMessage());
        }
        scanner.nextLine();
    }

    private void deleteMessage() {
        System.out.println("Message ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        this.messageService.deleteMessage(id);
        System.out.println("MESSAGE DELETED SUCCESSFULLY");
        scanner.nextLine();
    }

    private void showMessage() {
        this.messageService.getAllMessages().forEach(System.out::println);
    }

    private void showConversation() {
        System.out.println("First ID: ");
        Long first_id = Long.parseLong(scanner.nextLine());
        System.out.println("Second ID: ");
        Long second_id = Long.parseLong(scanner.nextLine());

        this.messageService.showConversation(first_id, second_id);
    }

    private void addFriendRequest() {
        System.out.println("FROM: ");
        Long from = Long.parseLong(scanner.nextLine());

        System.out.println("TO: ");
        Long to = Long.parseLong(scanner.nextLine());

        try {
            this.requestsService.addRequest(from, to);
            System.out.println("REQUEST SENT SUCCESSFULLY");
        } catch (ValidationException | IllegalArgumentException error) {
            System.out.println(error.getMessage());
        }
        scanner.nextLine();
    }

    private void showAllFriendRequests() {
        this.requestsService.getAllRequests().forEach(System.out::println);
    }

    private void showAllFriendRequestsForAnUser() {
        System.out.println("Give the id of the user");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.println(this.requestsService.getAllRequestsForAnUser(id));
    }

    private void acceptOrRejectRequest() {
        System.out.println("Give the id of the friend request:");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.println("Do you want to accept it or to reject it?");
        String status = scanner.nextLine();

        try {
            this.requestsService.updateRequest(id, status);
        } catch (ValidationException | IllegalArgumentException error) {
            System.out.println(error.getMessage());
        }
    }


}
