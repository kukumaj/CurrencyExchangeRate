package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class CurrencyExchangeRate {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the currency code (or 'close' to exit): ");
            String currency = scanner.next().toUpperCase();

            if (currency.equals("CLOSE")) {
                break;
            }

            String url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.lines().collect(Collectors.joining());
            in.close();

            JSONArray data = new JSONArray(response);
            JSONObject table = data.getJSONObject(0);
            JSONArray rates = table.getJSONArray("rates");

            JSONObject rate = null;
            for (int i = 0; i < rates.length(); i++) {
                JSONObject r = rates.getJSONObject(i);
                if (r.getString("code").equals(currency)) {
                    rate = r;
                    break;
                }
            }

            if (rate != null) {
                String currencyName = rate.getString("currency");
                String date = table.getString("effectiveDate");
                double exchangeRate = rate.getDouble("mid");
                System.out.println(String.format("The exchange rate for %s (%s) on %s is %.4f.", currencyName, currency, date, exchangeRate));
            } else {
                System.out.println("Currency not found.");
            }
        }
    }
}