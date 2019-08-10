package control;

//Замечательный GLFW не разрешает кинуть ControlException из колбэка
//Этот класс — костыль на него
//Использовать исключительно как обёртку над нормальным исключением

import menu.MenuException;

class InputCallbackException extends RuntimeException{
    private Exception exc;

    public InputCallbackException(MenuException exception) {
        exc = exception;
    }

    public InputCallbackException(ControlException exception) {
        exc = exception;
    }

    public Exception getException() {
        return exc;
    }
}
