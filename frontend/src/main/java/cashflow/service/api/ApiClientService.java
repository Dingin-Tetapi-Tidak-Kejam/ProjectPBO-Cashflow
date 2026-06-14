package cashflow.service.api;

import cashflow.dto.AuthResponse;
import cashflow.dto.SummaryResponse;
import cashflow.dto.TransactionResponse;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiClientService {

    private static final String BASE_URL = "http://localhost:8080/api";

    private static final CookieManager COOKIE_MANAGER = new CookieManager();
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .cookieHandler(COOKIE_MANAGER)
            .build();

    static {
        COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }

    public AuthResponse register(String username, String email, String password) throws IOException, InterruptedException {
        String json = String.format(
                "{\"username\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                escapeJson(username),
                escapeJson(email),
                escapeJson(password)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("REGISTER RESPONSE: " + response.body());

        if (response.statusCode() == 200 || response.statusCode() == 400) {
            return parseAuthResponse(response.body());
        }

        throw new RuntimeException("Register gagal. Status: " + response.statusCode() + " | Body: " + response.body());
    }

    public AuthResponse login(String usernameOrEmail, String password) throws IOException, InterruptedException {
        String json = String.format(
                "{\"usernameOrEmail\":\"%s\",\"password\":\"%s\"}",
                escapeJson(usernameOrEmail),
                escapeJson(password)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("LOGIN RESPONSE: " + response.body());

        if (response.statusCode() == 200 || response.statusCode() == 400) {
            return parseAuthResponse(response.body());
        }

        throw new RuntimeException("Login gagal. Status: " + response.statusCode() + " | Body: " + response.body());
    }

    public boolean logout() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/logout"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public SummaryResponse getSummary() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transactions/summary"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gagal ambil summary. Status: " + response.statusCode() + " | Body: " + response.body());
        }

        return parseSummaryResponse(response.body());
    }

    public List<TransactionResponse> getAllTransactions() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transactions"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gagal ambil transaksi. Status: " + response.statusCode() + " | Body: " + response.body());
        }

        return parseTransactionList(response.body());
    }

    public TransactionResponse createTransaction(String transactionType, double amount, String date,
                                                 String description, String detail)
            throws IOException, InterruptedException {

        String json = String.format(
                "{\"transactionType\":\"%s\",\"amount\":%s,\"date\":\"%s\",\"description\":\"%s\",\"detail\":\"%s\"}",
                escapeJson(transactionType),
                amount,
                escapeJson(date),
                escapeJson(description == null ? "" : description),
                escapeJson(detail == null ? "" : detail)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transactions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gagal membuat transaksi. Status: " + response.statusCode() + " | Body: " + response.body());
        }

        return parseTransactionResponse(response.body());
    }

    public List<TransactionResponse> searchTransactions(String keyword) throws IOException, InterruptedException {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transactions/search?keyword=" + encodedKeyword))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gagal search transaksi. Status: " + response.statusCode() + " | Body: " + response.body());
        }

        return parseTransactionList(response.body());
    }

    public boolean deleteTransaction(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/transactions/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private AuthResponse parseAuthResponse(String json) {
        AuthResponse response = new AuthResponse();

        response.setSuccess(json.contains("\"success\":true"));
        response.setMessage(extractJsonValue(json, "message"));
        response.setUsername(extractJsonValue(json, "username"));
        response.setEmail(extractJsonValue(json, "email"));
        response.setRole(extractJsonValue(json, "role"));
        response.setUserId(parseLongValue(extractJsonNumber(json, "userId")));

        return response;
    }

    private SummaryResponse parseSummaryResponse(String json) {
        SummaryResponse response = new SummaryResponse();
        response.setTotalIncome(parseDoubleValue(extractJsonNumberOrDecimal(json, "totalIncome")));
        response.setTotalExpense(parseDoubleValue(extractJsonNumberOrDecimal(json, "totalExpense")));
        response.setBalance(parseDoubleValue(extractJsonNumberOrDecimal(json, "balance")));
        return response;
    }

    private TransactionResponse parseTransactionResponse(String json) {
        TransactionResponse response = new TransactionResponse();
        response.setId(parseLongValue(extractJsonNumber(json, "id")));
        response.setTransactionType(extractJsonValue(json, "transactionType"));
        response.setAmount(parseDoubleValue(extractJsonNumberOrDecimal(json, "amount")));
        response.setDate(extractJsonValue(json, "date"));
        response.setDescription(extractJsonValue(json, "description"));
        response.setDetail(extractJsonValue(json, "detail"));
        return response;
    }

    private List<TransactionResponse> parseTransactionList(String json) {
        List<TransactionResponse> list = new ArrayList<>();

        String trimmed = json.trim();
        if (trimmed.isEmpty() || trimmed.equals("[]")) {
            return list;
        }

        Pattern objectPattern = Pattern.compile("\\{[^{}]*}");
        Matcher matcher = objectPattern.matcher(trimmed);

        while (matcher.find()) {
            String obj = matcher.group();
            TransactionResponse response = parseTransactionResponse(obj);

            if (response.getTransactionType() != null || response.getDescription() != null || response.getDetail() != null) {
                list.add(response);
            }
        }

        return list;
    }

    private String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractJsonNumber(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractJsonNumberOrDecimal(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Long parseLongValue(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDoubleValue(String value) {
        if (value == null || value.isBlank()) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}