package com.wk.base.servlet;


import com.wk.base.annotation.Autowired;
import com.wk.base.annotation.Controller;
import com.wk.base.annotation.RequestMapping;
import com.wk.base.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * MVC请求分发中转
 */
public class DispatcherServlet extends HttpServlet {
    private Properties properties = new Properties();
    //存放class文件的路径
    private List<String> classNames = new ArrayList<>();
    //通过名字进行映射
    private Map<String, Object> ioc = new HashMap<>();
    //通过类型进行映射
    private Map<String, Object> iocType = new HashMap<>();
    //url到方法的映射
    private Map<String, Method> handlerMapping = new HashMap<>();
    //url到控制函数的映射
    private Map<String, Object> controllerMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        //1. 加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //2. 初始化所有相关的类, 扫描用户设定的包下面的所有的类
        doScanner(properties.getProperty("scanPackages"));
        //3. 拿到扫描到的类, 通过反射机制,实例化,并且放到容器IOC中,beanName 默认是首字母小写
        doInstance();
        //4. 初始化handlermapping(将url和method对应上)
        initHandlerMapping();
        //自动注入
        //autoInject();
    }

    private void autoInject() {
        if (ioc.isEmpty() && iocType.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : iocType.entrySet()) {
            //获取变量
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                //private、protected修饰的变量可访问
                field.setAccessible(true);

                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Object instance = null;
                String beanName = field.getType().getName();
                String simpleName = toLowerFirstWord(field.getType().getSimpleName());
                //首先根据Type注入，没有实例时根据Name，否则抛出异常
                if (iocType.containsKey(beanName)) {
                    instance = iocType.get(beanName);
                } else if (ioc.containsKey(simpleName)) {
                    instance = ioc.get(simpleName);
                } else {
                    throw new RuntimeException("not find class to autowire");
                }
                try {
                    //向obj对象的这个Field设置新值value,依赖注入
                    field.set(entry.getValue(), instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty() && iocType.isEmpty()) {
            return;
        }
        try {
            for (Map.Entry<String, Object> entry : iocType.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                //拼接url时, 时controller上的url加上方法上的url
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestAnno = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestAnno.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping methodRequestAnno = method.getAnnotation(RequestMapping.class);
                    String url = methodRequestAnno.value().trim();
                    //去除多个 //的情况
                    url = (baseUrl + "/" + url).replaceAll("/+", "/");

                    //存储url对应的类实例和方法
                    handlerMapping.put(url, method);
                    controllerMap.put(url, clazz.newInstance());
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (String className : classNames) {
            try {
                // 通过反射  实例化类
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Controller annotation = clazz.getAnnotation(Controller.class);
                    String beanName = annotation.value().trim();
                    if ("".equals(beanName)) {
                        beanName = toLowerFirstWord(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    //放入到名字容器和类型容器中
                    ioc.put(beanName, instance);
                    iocType.put(clazz.getName(), instance);
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service annotation = clazz.getAnnotation(Service.class);
                    String beanName = annotation.value().trim();
                    if ("".equals(beanName)) {
                        beanName = toLowerFirstWord(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    iocType.put(clazz.getName(), instance);
                    //如果service实现了接口, 可以使用接口作为key
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class ii : interfaces) {
                        ioc.put(toLowerFirstWord(ii.getSimpleName()), instance);
                        iocType.put(ii.getName(), instance);
                    }
                } else {
                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void doScanner(String scanPackages) {
        //把所有的 . 替换为 /
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackages.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                //递归扫描包
                doScanner(scanPackages + "." + file.getName());
            } else {
                //把文件的路径添加到list中
                String className = scanPackages + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        //把web.xml中测contextConfigLoction对应的值的对应的文件载入进来
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            //加载配置文件内容
            this.properties.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //处理请求
            doDispatch(request, response);
        } catch (Exception e) {
            response.getWriter().write("500!! server Exception");
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) {
        if (handlerMapping.isEmpty()) {
            return;
        }
        try {
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            System.out.println("requestURI is :" + requestURI + "  contextPath is:" + contextPath);
            //拼接url, 把多个/替换为一个
            requestURI = requestURI.replace(contextPath, "").replaceAll("/+", "/");
            if (!this.handlerMapping.containsKey(requestURI)) {
                response.getWriter().write("404 NOT FOUND");
                return;
            }
            //得到此url对应的方法
            Method method = this.handlerMapping.get(requestURI);
            //获取方法参数列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            //获取请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();

            //保存参数值
            Object[] paramValues = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                //根据参数名称,做某些处理
                String requestParamName = parameterTypes[i].getSimpleName();

                if (requestParamName.equals("HttpServletRequest")) {
                    //参数类型已直到,强转类型
                    paramValues[i] = request;
                    continue;
                }
                if (requestParamName.equals("HttpServletResponse")) {
                    paramValues[i] = response;
                    continue;
                }
                if (requestParamName.equals("String")) {
                    for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                        String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replace(",\\s", ",");
                        paramValues[i] = value;
                    }
                }

            }
            method.invoke(this.controllerMap.get(requestURI), paramValues);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * 把字符串首字母小写并返回
     *
     * @param name
     * @return
     */
    private String toLowerFirstWord(String name) {
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
}
