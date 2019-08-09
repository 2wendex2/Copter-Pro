package control;

//Замечательный GLFW не разрешает кинуть ControlException из колбэка
//Этот класс — костыль на него
//Использовать исключительно как обёртку над нормальным исключением

public class GLFWCallbackException extends RuntimeException{
    public GLFWCallbackException(Throwable exception) {
        super(exception);
    }
}
