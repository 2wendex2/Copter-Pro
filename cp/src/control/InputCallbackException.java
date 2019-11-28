package control;

//Замечательный GLFW не разрешает кинуть ControlException из колбэка
//Этот класс — костыль на него
//Использовать исключительно как обёртку над нормальным исключением

class InputCallbackException extends RuntimeException{
    private ControlException exc;

    public InputCallbackException(ControlException exception) {
        exc = exception;
    }

    public ControlException getException() {
        return exc;
    }
}
