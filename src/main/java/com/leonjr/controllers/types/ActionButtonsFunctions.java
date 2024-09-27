package com.leonjr.controllers.types;

public record ActionButtonsFunctions(
        Runnable select,
        Runnable translate,
        Runnable rotate,
        Runnable scale,
        Runnable reflect,
        Runnable delete) {
}
