package com.daradat.boot.solutionization.logicTransition.reflect;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class URLClassLoaderRegistry {
    private static Map<URL, URLClassLoader> registry = new ConcurrentHashMap<URL, URLClassLoader>();

    /**
     * local File 유형 (폴더 혹은 jar파일)을 사용하여 {@link URLClassLoader}를 리턴한다.
     * @param dir
     * @return
     */
    public URLClassLoader getClassLoader(File dir){
        try {
            URL url = dir.toURI().toURL();
            return getClassLoader(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Given URL is malformed." + dir);
        }
    }

    /**
     * URL 표현 spec 문자열을 사용하여  {@link URLClassLoader}를 리턴한다.
     * @param spec URL로 표현할 수 있는 경로 (local 폴더, local jar 파일, 원격 jar 파일의 경로)
     * @return
     */
    public URLClassLoader getClassLoader(String spec){
        try {
            File file = new File(spec);
            if (file.exists()) {
                return getClassLoader(file);
            }
            URL url = new URL(spec);
            return getClassLoader(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Given URL is malformed." + spec);
        }
    }

    /**
     * {@link URL}을 사용하여  {@link URLClassLoader}를 리턴한다.
     * @param url (local 폴더, local jar 파일, 원격 jar 파일의 {@link URL})
     * @return
     */
    public URLClassLoader getClassLoader(URL url){
        URLClassLoader classloader = registry.get(url);
        if (classloader == null) {
            classloader = createURLClassLoader(url);
            registry.put(url, classloader);
        }
        return classloader;
    }

    /**
     * 캐싱하고 있던 {@link URLClassLoader}를 제거한다.
     * @param dir
     * @return
     */
    public URLClassLoader removeClassLoader(File dir){
        try {
            URL url = dir.toURI().toURL();
            return removeClassLoader(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Given URL is malformed." + dir);
        }
    }

    /**
     * 캐싱하고 있던 {@link URLClassLoader}를 제거한다.
     * @param spec
     * @return
     */
    public URLClassLoader removeClassLoader(String spec){
        try {
            URL url = new URL(spec);
            return removeClassLoader(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Given URL is malformed." + spec);
        }
    }

    /**
     * 캐싱하고 있던 {@link URLClassLoader}를 제거한다.
     * @param url
     * @return
     */
    public URLClassLoader removeClassLoader(URL url){
        URLClassLoader classloader = registry.get(url);
        if (classloader != null) {
            registry.remove(url, classloader);
        }
        return classloader;
    }

    /**
     * {@link URL}을 사용하여  {@link URLClassLoader}를 생성하고 리턴한다.
     * @param url
     * @return
     */
    protected URLClassLoader createURLClassLoader(URL url){
        try {
            ClassLoader parent = Thread.currentThread().getContextClassLoader();
            URLClassLoader classloader = new URLClassLoader(new URL[] { url }, parent);
            return classloader;
        } catch (Exception e) {
            return new URLClassLoader(new URL[] { url });
        }
    }

    /**
     * singleton으로 구성된 {@link URLClassLoaderRegistry} 인스턴스를 리턴한다.
     */
    public static URLClassLoaderRegistry getInstance(){
        return InstanceHolder.instance;
    }

    /**
     * singleton 인스턴스 구성체
     *
     */
    private static class InstanceHolder {

        private static final URLClassLoaderRegistry instance = new URLClassLoaderRegistry();

    }
}
