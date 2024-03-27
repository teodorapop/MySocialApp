package com.example.mysocialapp;

import com.example.mysocialapp.domain.Friendship;
import com.example.mysocialapp.domain.Message;
import com.example.mysocialapp.domain.Request;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.FriendshipValidator;
import com.example.mysocialapp.domain.validators.MessageValidator;
import com.example.mysocialapp.domain.validators.RequestValidator;
import com.example.mysocialapp.domain.validators.UserValidator;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.repo.database.FriendshipDatabaseRepository;
import com.example.mysocialapp.repo.database.MessageDatabaseRepository;
import com.example.mysocialapp.repo.database.RequestsDatabaseRepository;
import com.example.mysocialapp.repo.database.UserDatabaseRepository;
import com.example.mysocialapp.service.FriendshipsService;
import com.example.mysocialapp.service.MessageService;
import com.example.mysocialapp.service.RequestsService;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.ui.UI;
import com.example.mysocialapp.utils.DBConstants;

public class Main {
    public static void main(String[] args) {
//        UserValidator userValidator = new UserValidator(); // Inițializează validatorul
//        FriendshipValidator friendshipValidator = new FriendshipValidator();
//        InMemoryRepository<Long, User> userRepository = new InMemoryRepository<>(userValidator); // Creează repository-ul
//        InMemoryRepository<Long, Friendship> friendshipRepository = new InMemoryRepository<>(friendshipValidator);
//        UserService userService = new UserService(userRepository);
//        FriendshipsService friendshipsService = new FriendshipsService(friendshipRepository, userRepository);
//
//        UI ui = new UI(userService,friendshipsService);
//        ui.menu();

        Repository<Long, User> userRepository = new UserDatabaseRepository(DBConstants.url, DBConstants.username, DBConstants.password, new UserValidator());
        UserService usersService = new UserService(userRepository);
        Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository(DBConstants.url,DBConstants.username, DBConstants.password, new FriendshipValidator());
        FriendshipsService friendshipsService = new FriendshipsService(friendshipRepository, userRepository);

        Repository<Long, Message> messageRepository = new MessageDatabaseRepository(DBConstants.url, DBConstants.username, DBConstants.password, new MessageValidator());
        MessageService messageService = new MessageService(messageRepository);

        Repository<Long, Request> requestRepository = new RequestsDatabaseRepository(DBConstants.url, DBConstants.username, DBConstants.password, new RequestValidator());
        RequestsService requestsService = new RequestsService(requestRepository, userRepository, friendshipRepository);

        UI ui = new UI(usersService, friendshipsService, messageService, requestsService);
//        ui.menu();
        LogInApplication.main(args);

    }
}
