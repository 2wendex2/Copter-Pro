package ru.w2tksoft.cp.config;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.Reader;

public class TestResource{
    public TestResource() {}

    @Test @Ignore
    public void testDefaultFolders() throws Exception {
        Resource resource = new Resource(null, null, null,
                TestResource.class.getClassLoader(), null);
        Reader reader = resource.getInputTextResource("target/test-files/config/Resource/testInputText");

        char[] buf = new char[2048];
        int i = reader.read(buf, 0, buf.length);
        String s = new String(buf, 0, i);
        Assert.assertEquals(s, "input text test");
    }

    @Test @Ignore
    public void testDefaultDataAndSettingsFolders() throws Exception {
        Resource resource = new Resource("target/test-files/config/Resource", null, null,
                TestResource.class.getClassLoader(), null);
        Reader reader = resource.getInputTextResource("testInputText");

        char[] buf = new char[2048];
        int i = reader.read(buf, 0, buf.length);
        String s = new String(buf, 0, i);
        Assert.assertEquals(s, "input text test");


    }
}