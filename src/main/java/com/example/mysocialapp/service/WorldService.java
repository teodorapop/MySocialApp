package com.example.mysocialapp.service;

import com.example.mysocialapp.domain.Friendship;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.repo.Repository;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldService {
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    World world;

    public WorldService(Repository<Long, Friendship> repo, Repository<Long, User> repo2) {
        this.friendshipRepository = repo;
        this.userRepository = repo2;
        world = buildWorld();
    }

    public int fridshipE(Long id1, Long id2) {

        Map<User, LocalDateTime> friends = friendshipsOfAnUser(id1);
        for (Map.Entry<User, LocalDateTime> friend : friends.entrySet()) {
            if (friend.getKey().getId().equals(id2))
                return 1;
        }
        return 0;
    }

    public Map<User, LocalDateTime> friendshipsOfAnUser(Long id) {

        HashSet<Friendship> all = (HashSet<Friendship>) this.friendshipRepository.getAll();

        Predicate<Friendship> p = f -> f.getId1().equals(id);
        Stream<Friendship> friendships1 = all.stream().filter(p);

        Predicate<Friendship> p2 = f -> f.getId2().equals(id);
        Stream<Friendship> friendships2 = all.stream().filter(p2);

        Stream<Pair<User, LocalDateTime>> res1 = friendships1.map(
                f -> new Pair<>(this.userRepository.getOne(f.getId2()), f.getDate()));
        Stream<Pair<User, LocalDateTime>> res2 = friendships2.map(
                f -> new Pair<>(this.userRepository.getOne(f.getId1()), f.getDate()));

        Map<User, LocalDateTime> r1 = res1.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        Map<User, LocalDateTime> r2 = res2.collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        r1.putAll(r2);

        return r1;
    }

    public Map<User, LocalDateTime> friendshipsOfAnUser(Long id, int month) {
        Map<User, LocalDateTime> allFriends = this.friendshipsOfAnUser(id);

        return allFriends.entrySet().stream().filter(p -> p.getValue().getMonthValue() == month).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int numberOfCommunities() {
        world = buildWorld();
        int result = 0;
        for (User u : userRepository.getAll()) {
            if (u.getFriends().isEmpty()) result = result + 1;
        }
        return result + world.findTheNumberOfCommunities();
    }

    public List<User> getBiggestCommunity() {
        world = buildWorld();
        List<User> biggestCommunity = new ArrayList<>();
        for (User u : userRepository.getAll()) {
            if (world.getBiggestCommunity().contains(u.getId())) {
                biggestCommunity.add(u);
            }
        }
        return biggestCommunity;
    }

    private World buildWorld() {
        Long max = 0L;
        assert userRepository != null;
        for (User u : userRepository.getAll()) {
            if (u.getId() > max) {
                max = u.getId();
            }
        }
        World world = new World(Math.toIntExact(max) + 1);
        assert this.friendshipRepository != null;
        for (Friendship f : this.friendshipRepository.getAll()) {
            world.addFriendshipInWorld(f.getId1(), f.getId2());
        }
        return world;
    }


    /**
     * class which can include the relationships between all the users - it is a graph
     */
    private static class World {
        int numberOfCommunities = 0;
        private final List<List<Long>> relationships;
        private final boolean[] visited;
        private final int numberOfUsers;
        private final List<List<Long>> communities = new ArrayList<>();
        private final List<Long> biggestCommunity = new ArrayList<>();

        /**
         * @param nodes - the number of users that exist
         *              relationships - the friends lists of all the users
         */
        public World(int nodes) {
            relationships = new ArrayList<>();
            visited = new boolean[nodes];
            this.numberOfUsers = nodes;

            relationships.add(0, null);

            for (int i = 1; i < nodes; i++) {
                relationships.add(i, new ArrayList<>());
            }
        }

        public void addFriendshipInWorld(Long a, Long b) {
            relationships.get(a.intValue()).add(b);
            relationships.get(b.intValue()).add(a);
        }

        public int findTheNumberOfCommunities() {
            calculateNumberOfCommunities();
            return numberOfCommunities;
        }

        public void calculateNumberOfCommunities() {
            for (int i = 1; i < numberOfUsers; i++) {
                if (!visited[i]) {
                    if (!relationships.get(i).isEmpty()) {
                        dfs(i);
                        numberOfCommunities++;
                    }
                }
            }
            Arrays.fill(visited, false);
        }

        public List<Long> getBiggestCommunity() {
            findCommunities();
            for (List<Long> community : this.communities) {
                if (biggestCommunity.size() < community.size()) {
                    biggestCommunity.clear();
                    biggestCommunity.addAll(community);
                }
            }
            Arrays.fill(visited, false);
            return biggestCommunity;
        }

        private void dfs(int start) {
            Stack<Long> stack = new Stack<>();

            stack.push((long) start);
            visited[start] = true;

            while (!stack.isEmpty()) {
                Long node = stack.pop();

                List<Long> neighboursList = relationships.get(Math.toIntExact(node));

                for (Long neighbour : neighboursList) {
                    if (!visited[Math.toIntExact(neighbour)]) {
                        stack.push(neighbour);
                        visited[Math.toIntExact(neighbour)] = true;
                    }
                }
            }
        }

        private void findCommunities() {
            calculateNumberOfCommunities();
            for (int i = 0; i < numberOfCommunities; i++) {
                this.communities.add(i, new ArrayList<>());
            }

            int position = numberOfCommunities;
            for (int i = 1; i < numberOfUsers; i++) {
                if (!visited[i]) {
                    if (!relationships.get(i).isEmpty()) {
                        position--;
                        this.communities.get(position).add((long) i);
                        makeACommunity(i, position);
                    }
                }
            }
        }

        private void makeACommunity(int start, int position) {
            Stack<Long> stack = new Stack<>();

            stack.push((long) start);
            visited[start] = true;

            while (!stack.isEmpty()) {
                Long node = stack.pop();

                List<Long> neighboursList = relationships.get(Math.toIntExact(node));

                for (Long neighbour : neighboursList) {
                    if (!visited[Math.toIntExact(neighbour)]) {
                        this.communities.get(position).add(neighbour);
                        stack.push(neighbour);
                        visited[Math.toIntExact(neighbour)] = true;
                    }
                }
            }
        }
    }
}