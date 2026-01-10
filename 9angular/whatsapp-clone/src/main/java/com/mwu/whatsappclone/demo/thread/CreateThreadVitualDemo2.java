package com.mwu.whatsappclone.demo.thread;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class CreateThreadVitualDemo2 {
    public static void main(String[] args) {
       List<String> res =  withCompletableFuture();
         System.out.println("Results: " + res);


    }

    private static List<String> withCompletableFuture() {
        List<String> usernames = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");
        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = usernames.stream()
                    .map(u -> CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(1000); // Simulate a blocking operation
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        System.out.println("Processed user: " + u);
                        return "Processed " + u;
                    }, executor))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            return futures.stream().map(CompletableFuture::join).toList();



        }

    }
}
