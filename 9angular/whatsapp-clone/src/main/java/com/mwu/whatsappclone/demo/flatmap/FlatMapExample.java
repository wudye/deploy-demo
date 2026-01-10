package com.mwu.whatsappclone.demo.flatmap;

import java.util.Optional;

public class FlatMapExample {
    public static void main(String[] args) {
        Optional<String> emailOpt = Optional.of("user@example.com");

        // 使用 map 会得到 Optional<Optional<Username>>
        Optional<Optional<UsernameDemo>> nested = emailOpt.map(UsernameDemo::of);
        System.out.println("map -> " + nested); // 输出: map -> Optional[Optional[Username{user@example.com}]]

        // 使用 flatMap 会扁平化为 Optional<Username>
        Optional<UsernameDemo> flat = emailOpt.flatMap(UsernameDemo::of);
        System.out.println("flatMap -> " + flat); // 输出: flatMap -> Optional[Username{user@example.com}]

        // 当输入为空或无效时，flatMap 可以直接得到 Optional.empty()
        Optional<String> emptyEmail = Optional.of("");
        System.out.println("empty flatMap -> " + emptyEmail.flatMap(UsernameDemo::of)); // Optional.empty
    }
}
