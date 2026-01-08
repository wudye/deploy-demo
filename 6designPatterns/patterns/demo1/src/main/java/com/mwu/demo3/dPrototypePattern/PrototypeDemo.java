package com.mwu.demo3.dPrototypePattern;

import java.util.Random;

public class PrototypeDemo {
    private static int MAX_COUNT = 6;

    public static void main(String[] args) {
        int i = 0;

        Mail mail = new Mail(new AdvTemplate());

        mail.setTail("tail");

        while (i < MAX_COUNT) {
            Mail mailClone = mail.clone();

            mailClone.setAppellation(getRandString(5) + "mister");
            mailClone.setReceiver(getRandString(5) + "@" + getRandString(8) + ".com");

            sendMail(mailClone);
            i++;
        }
    }

    private static void sendMail(Mail mail) {
        System.out.println("to:" + mail.getReceiver() + " from:" + mail.getAppellation() + " subject:" + mail.getSubject() + " tail:" + mail.getTail());
    }

    private static String getRandString(int i) {

        String source ="abcdefghijklmnopqrskuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int j = 0; j < i; j++) {
            int number = random.nextInt(source.length());
            stringBuffer.append(source.charAt(number));
        }
        return stringBuffer.toString();
    }
}
