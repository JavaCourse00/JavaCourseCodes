package jvm;

import java.util.Base64;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {

        new HelloClassLoader().findClass("lib.Hello").newInstance();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String helloBase64 = "yv66vgAAADQAHwoABwAQCQARABIIABMKABQAFQgAFgcAFwcAGAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBAAVoZWxsbwEACDxjbGluaXQ+AQAKU291cmNlRmlsZQEACkhlbGxvLmphdmEMAAgACQcAGQwAGgAbAQAdSGVsbG8gY2xhc3Mgc2F5IGhlbGxvIG1ldGhvZC4HABwMAB0AHgEAGEhlbGxvIENsYXNzIEluaXRpYWxpemVkIQEACWxpYi9IZWxsbwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYAIQAGAAcAAAAAAAMAAQAIAAkAAQAKAAAAHQABAAEAAAAFKrcAAbEAAAABAAsAAAAGAAEAAAADAAEADAAJAAEACgAAACUAAgABAAAACbIAAhIDtgAEsQAAAAEACwAAAAoAAgAAAAgACAAJAAgADQAJAAEACgAAACUAAgAAAAAACbIAAhIFtgAEsQAAAAEACwAAAAoAAgAAAAUACAAGAAEADgAAAAIADw==+AQAKU291cmNlRmlsZQEACkhlbGxvLmphdmEMAAgACQcAGQwAGgAbAQAdSGVsbG8gY2xhc3Mgc2F5IGhlbGxvIG1ldGhvZC4HABwMAB0AHgEAGEhlbGxvIENsYXNzIEluaXRpYWxpemVkIQEACWxpYi9IZWxsbwEAEGphdmEvbGFuZy9PYmplY3QBABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYAIQAGAAcAAAAAAAMAAQAIAAkAAQAKAAAAHQABAAEAAAAFKrcAAbEAAAABAAsAAAAGAAEAAAADAAEADAAJAAEACgAAACUAAgABAAAACbIAAhIDtgAEsQAAAAEACwAAAAoAAgAAAAgACAAJAAgADQAJAAEACgAAACUAAgAAAAAACbIAAhIFtgAEsQAAAAEACwAAAAoAAgAAAAUACAAGAAEADgAAAAIADw==";
        byte[] bytes = decode(helloBase64);
        return defineClass(name,bytes,0,bytes.length);
    }

    public byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
