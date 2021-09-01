package demo;

public class ProviderImpl implements Provider{
    @Override
    public String hello() {
       return "hello,world.";
    }

    @Override
    public String say() {
        return "hahahhaha....";
    }
}
