import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CodeKeepOOP {

    static class CodeKeepException extends Exception {
        public CodeKeepException(String message) {
            super(message);
        }
    }

    static final class Platform {
        private final int platformId;
        private final String platformName;
        private final String websiteUrl;

        public Platform(int platformId, String platformName, String websiteUrl) {
            this.platformId = platformId;
            this.platformName = platformName;
            this.websiteUrl = websiteUrl;
        }

        public int getPlatformId() { return platformId; }
        public String getPlatformName() { return platformName; }
        public String getWebsiteUrl() { return websiteUrl; }
    }

    static final class Problem {
        private final int problemId;
        private final String problemName;
        private final int difficultyScore;
        private final String platformName;

        public Problem(int problemId, String problemName, int difficultyScore, String platformName) {
            this.problemId = problemId;
            this.problemName = problemName;
            this.difficultyScore = difficultyScore;
            this.platformName = platformName;
        }

        public int getProblemId() { return problemId; }
        public String getProblemName() { return problemName; }
        public int getDifficultyScore() { return difficultyScore; }
        public String getPlatformName() { return platformName; }
    }

    static final class Contest {
        private final int contestId;
        private final String contestName;
        private final LocalDate contestDate;
        private final LocalTime contestStart;
        private final int durationMinutes;
        private final String platformName;
        private final List<Problem> includedProblems;

        public Contest(int contestId, String contestName, LocalDate contestDate,
                       LocalTime contestStart, int durationMinutes, String platformName,
                       List<Problem> includedProblems) {
            this.contestId = contestId;
            this.contestName = contestName;
            this.contestDate = contestDate;
            this.contestStart = contestStart;
            this.durationMinutes = durationMinutes;
            this.platformName = platformName;
            this.includedProblems = List.copyOf(includedProblems);
        }

        public int getContestId() { return contestId; }
        public String getContestName() { return contestName; }
        public LocalDate getContestDate() { return contestDate; }
        public LocalTime getContestStart() { return contestStart; }
        public int getDurationMinutes() { return durationMinutes; }
        public String getPlatformName() { return platformName; }
        public List<Problem> getIncludedProblems() { return includedProblems; }

        public LocalTime getContestEnd() {
            return contestStart.plusMinutes(durationMinutes);
        }
    }

    static final class UserProfile {
        private final int userId;
        private final String profileLink;

        public UserProfile(int userId, String profileLink) {
            this.userId = userId;
            this.profileLink = profileLink;
        }

        public int getUserId() { return userId; }
        public String getProfileLink() { return profileLink; }
    }

    static final class UserSummary {
        private final int userId;
        private final String name;
        private final String email;
        private final List<UserProfile> profiles;

        public UserSummary(int userId, String name, String email, List<UserProfile> profiles) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.profiles = List.copyOf(profiles);
        }

        public int getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public List<UserProfile> getProfiles() { return profiles; }
    }

    static final class Participation {
        private final int userId;
        private final String userName;
        private final int contestId;
        private final String contestName;
        private final int rank;
        private final int score;

        public Participation(int userId, String userName, int contestId,
                             String contestName, int rank, int score) {
            this.userId = userId;
            this.userName = userName;
            this.contestId = contestId;
            this.contestName = contestName;
            this.rank = rank;
            this.score = score;
        }

        public int getUserId() { return userId; }
        public String getUserName() { return userName; }
        public int getContestId() { return contestId; }
        public String getContestName() { return contestName; }
        public int getRank() { return rank; }
        public int getScore() { return score; }
    }

    static final class DashboardData {
        private final List<Platform> platforms;
        private final List<UserSummary> users;
        private final List<Contest> contests;
        private final List<Problem> problems;
        private final List<Participation> participations;

        public DashboardData(List<Platform> platforms, List<UserSummary> users,
                             List<Contest> contests, List<Problem> problems,
                             List<Participation> participations) {
            this.platforms = List.copyOf(platforms);
            this.users = List.copyOf(users);
            this.contests = List.copyOf(contests);
            this.problems = List.copyOf(problems);
            this.participations = List.copyOf(participations);
        }

        public List<Platform> getPlatforms() { return platforms; }
        public List<UserSummary> getUsers() { return users; }
        public List<Contest> getContests() { return contests; }
        public List<Problem> getProblems() { return problems; }
        public List<Participation> getParticipations() { return participations; }
    }

    interface DashboardRepository {
        DashboardData loadDashboardData();
        void saveUser(int userId, String name, String email, List<String> profileLinks) throws CodeKeepException;
        void saveParticipation(int userId, int contestId, int rank, int score) throws CodeKeepException;
    }

    static class InMemoryDashboardRepository implements DashboardRepository {
        private final Map<Integer, Platform> platforms = new LinkedHashMap<>();
        private final Map<Integer, UserSummary> users = new LinkedHashMap<>();
        private final Map<Integer, Problem> problems = new LinkedHashMap<>();
        private final Map<Integer, Contest> contests = new LinkedHashMap<>();
        private final List<Participation> participations = new ArrayList<>();

        public InMemoryDashboardRepository() {
            seedDefaultData();
        }

        private void seedDefaultData() {
            platforms.put(1, new Platform(1, "Codeforces", "https://codeforces.com"));
            platforms.put(2, new Platform(2, "LeetCode", "https://leetcode.com"));
            platforms.put(3, new Platform(3, "AtCoder", "https://atcoder.jp"));

            Problem p201 = new Problem(201, "Two Sum", 3, "LeetCode");
            Problem p202 = new Problem(202, "Binary Search", 5, "Codeforces");
            Problem p203 = new Problem(203, "Graph Traversal", 8, "Codeforces");
            Problem p204 = new Problem(204, "Dynamic Programming", 9, "Codeforces");
            problems.put(201, p201);
            problems.put(202, p202);
            problems.put(203, p203);
            problems.put(204, p204);

            contests.put(101, new Contest(101, "Codeforces Round 1", LocalDate.of(2026, 9, 1),
                    LocalTime.of(18, 0), 120, "Codeforces", List.of(p202, p203)));
            contests.put(102, new Contest(102, "Codeforces Round 2", LocalDate.of(2026, 9, 5),
                    LocalTime.of(20, 0), 150, "Codeforces", List.of(p203, p204)));
            contests.put(103, new Contest(103, "LeetCode Weekly Contest", LocalDate.of(2026, 9, 7),
                    LocalTime.of(10, 0), 90, "LeetCode", List.of(p201)));

            users.put(1, new UserSummary(1, "Siddharth", "siddharth@example.com", List.of(
                    new UserProfile(1, "https://codeforces.com/profile/siddharth"),
                    new UserProfile(1, "https://leetcode.com/u/siddharth")
            )));
            users.put(2, new UserSummary(2, "Rahul", "rahul@example.com", List.of(
                    new UserProfile(2, "https://codeforces.com/profile/rahul")
            )));
            users.put(3, new UserSummary(3, "Aman", "aman@example.com", List.of(
                    new UserProfile(3, "https://leetcode.com/u/aman")
            )));

            participations.add(new Participation(1, "Siddharth", 101, "Codeforces Round 1", 25, 850));
            participations.add(new Participation(2, "Rahul", 101, "Codeforces Round 1", 42, 720));
            participations.add(new Participation(1, "Siddharth", 102, "Codeforces Round 2", 18, 910));
            participations.add(new Participation(3, "Aman", 103, "LeetCode Weekly Contest", 15, 880));
        }

        @Override
        public DashboardData loadDashboardData() {
            return new DashboardData(
                    new ArrayList<>(platforms.values()),
                    new ArrayList<>(users.values()),
                    new ArrayList<>(contests.values()),
                    new ArrayList<>(problems.values()),
                    new ArrayList<>(participations)
            );
        }

        @Override
        public void saveUser(int userId, String name, String email, List<String> profileLinks) throws CodeKeepException {
            if (users.containsKey(userId)) {
                throw new CodeKeepException("Duplicate Primary Key: User ID " + userId + " already exists.");
            }
            List<UserProfile> userProfiles = new ArrayList<>();
            for (String link : profileLinks) {
                if (link != null && !link.isBlank()) {
                    userProfiles.add(new UserProfile(userId, link.trim()));
                }
            }
            users.put(userId, new UserSummary(userId, name, email, userProfiles));
        }

        @Override
        public void saveParticipation(int userId, int contestId, int rank, int score) throws CodeKeepException {
            UserSummary user = users.get(userId);
            Contest contest = contests.get(contestId);

            if (user == null) {
                throw new CodeKeepException("Foreign Key Violation: User ID " + userId + " does not exist.");
            }
            if (contest == null) {
                throw new CodeKeepException("Foreign Key Violation: Contest ID " + contestId + " does not exist.");
            }
            for (Participation p : participations) {
                if (p.getUserId() == userId && p.getContestId() == contestId) {
                    throw new CodeKeepException("Composite Key Violation: Participation already recorded for User " + userId + " in Contest " + contestId);
                }
            }
            participations.add(new Participation(userId, user.getName(), contestId, contest.getContestName(), rank, score));
        }
    }

    static class DashboardService {
        private final DashboardRepository repository;

        public DashboardService(DashboardRepository repository) {
            this.repository = repository;
        }

        public DashboardData getDashboard() {
            return repository.loadDashboardData();
        }

        public void registerUser(int userId, String name, String email, List<String> profileLinks) throws CodeKeepException {
            if (name == null || name.isBlank()) {
                throw new CodeKeepException("Validation Error: User name cannot be empty.");
            }
            if (email == null || !email.contains("@")) {
                throw new CodeKeepException("Validation Error: Invalid email address.");
            }
            repository.saveUser(userId, name.trim(), email.trim(), profileLinks);
        }

        public void recordContestResult(int userId, int contestId, int rank, int score) throws CodeKeepException {
            if (rank <= 0) {
                throw new CodeKeepException("Validation Error: Rank must be a positive integer.");
            }
            if (score < 0) {
                throw new CodeKeepException("Validation Error: Score cannot be negative.");
            }
            repository.saveParticipation(userId, contestId, rank, score);
        }
    }

    public static void main(String[] args) {
        DashboardRepository repository = new InMemoryDashboardRepository();
        DashboardService service = new DashboardService(repository);
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("   CodeKeep - Competitive Programming Manager");
        System.out.println("==================================================");

        while (true) {
            System.out.println("\n1. View Dashboard Summary");
            System.out.println("2. Add New User");
            System.out.println("3. Record Contest Participation");
            System.out.println("4. Exit");
            System.out.print("Select option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> printDashboard(service.getDashboard());
                    case "2" -> {
                        System.out.print("Enter User ID: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter Profile Link (or press Enter to skip): ");
                        String link = scanner.nextLine();

                        service.registerUser(id, name, email, link.isBlank() ? List.of() : List.of(link));
                        System.out.println("[SUCCESS] User added successfully.");
                    }
                    case "3" -> {
                        System.out.print("Enter User ID: ");
                        int userId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Contest ID (101, 102, 103): ");
                        int contestId = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Rank: ");
                        int rank = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Score: ");
                        int score = Integer.parseInt(scanner.nextLine().trim());

                        service.recordContestResult(userId, contestId, rank, score);
                        System.out.println("[SUCCESS] Contest result recorded.");
                    }
                    case "4" -> {
                        System.out.println("Exiting CodeKeep.");
                        return;
                    }
                    default -> System.out.println("[ERROR] Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter valid numeric values.");
            } catch (CodeKeepException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private static void printDashboard(DashboardData data) {
        System.out.println("\n--- USERS & PROFILES ---");
        for (UserSummary u : data.getUsers()) {
            System.out.printf("ID: %d | Name: %-12s | Email: %-22s | Profiles: %d%n",
                    u.getUserId(), u.getName(), u.getEmail(), u.getProfiles().size());
        }

        System.out.println("\n--- CONTESTS & DERIVED END TIMES ---");
        for (Contest c : data.getContests()) {
            System.out.printf("ID: %d | %-25s | %s (%s - %s) | Problems: %d%n",
                    c.getContestId(), c.getContestName(), c.getPlatformName(),
                    c.getContestStart(), c.getContestEnd(), c.getIncludedProblems().size());
        }

        System.out.println("\n--- PARTICIPATION LEADERBOARD ---");
        data.getParticipations().stream()
                .sorted(Comparator.comparingInt(Participation::getRank))
                .forEach(p -> System.out.printf("Rank #%-3d | %-12s | %-25s | Score: %d%n",
                        p.getRank(), p.getUserName(), p.getContestName(), p.getScore()));
    }
}