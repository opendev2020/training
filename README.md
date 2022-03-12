# training入门使用文档

## 什么是training框架

training框架(以下简称training)是一个基于springboot+jsp的基础web框架，供**智能网络与信息安全研究室**web组成员学习，并以此框架为基础完成项目。

github地址：[点击进入](https://github.com/opendev2020/training)

## 使用所需基本知识

training框架主要由java，javascript，css三大语言，各语言占比如下图：

[![oJH9Ln.png](https://z3.ax1x.com/2021/12/01/oJH9Ln.png)](https://imgtu.com/i/oJH9Ln)

另外还需掌握html标记语言基础知识,sql数据库查询基本语句

若要熟练使用该框架，各语言技术栈最少做到如下要求：

### java

- **本地搭建java运行环境**
- **注意！该项目推荐jdk版本为1.8，若jdk版本不对，请在ide中修改对应版本，如果没有该版本，请在网络上下载**
- 熟练掌握javaSE，即java的基础知识，[点击这里进入我推荐的入门视频网站](https://www.bilibili.com/video/BV12J41137hu)
- 掌握基本的面向对象思想，对封装与继承两方面知识有正确的理解
- 初步掌握springboot框架的结构,熟悉mvc设计模式和各个包的用途
- 知道常用注解的作用，理解注解注入依赖的原理并知道怎么使用

### javascript

- 了解js如何绑定html中的dom元素的操作方法并且能够实际操作
- 学会使用jquery库并使用其中常用的各种函数
- 学会通过jquery的$.ajax实现前后端相互传值
- 初步掌握vue.js的初步入门知识

### css

- 学会基础使用即可

### html

- 学会使用bootsrap框架，调用bootstrap中的组件构建页面
- 配合css搭建修改页面
- 知道常用的标签及其一些常用的属性
- 使用form表单向后端传值

### mysql

- **本地搭建mysql运行环境**
- 学会并掌握最基础的增删改查语句，及主键约束，[点击这里进入我推荐的入门视频网站](https://www.bilibili.com/video/BV1Vt411z7wy?spm_id_from=333.999.0.0)

## 快速上手

### 运行

1. 解压training压缩包并用IDE打开项目文件夹:

[![oJHid0.png](https://z3.ax1x.com/2021/12/01/oJHid0.png)](https://imgtu.com/i/oJHid0)

2. 等待IDE将索引与依赖均编制完毕后——**因为国际网络问题，这可能需要较长时间**。检查pom.xml是否有飘红,即是否缺少相应的maven依赖，如果有报错飘红，请百度搜寻相关资料解决环境配置问题。

   [![oJHFoV.png](https://z3.ax1x.com/2021/12/01/oJHFoV.png)](https://imgtu.com/i/oJHFoV)

3. 打开 src/main/resources/application.properties，查看其配置信息，此时我们不难看到项目mysql的配置信息，如下:

   [![oJHpss.png](https://z3.ax1x.com/2021/12/01/oJHpss.png)](https://imgtu.com/i/oJHpss)

   根据该信息我们可知道当前连接的是本地mysql数据库，没有配置数据库,故我们将账号密码改成本地数据库的账号密码——**以自己本地mysql创建的账号密码为准**。

   ```properties
   #以自己本地的账户为准
   spring.datasource.username = root
   spring.datasource.password = root
   ```

4. 由连接mysql的url可知，我们连接的是本地数据库的training库，而我们本地并没有该数据库，也没有支持该项目运行对应的数据，所以要在本地建立一个training库并将相关数据填充进该库。

   建立training数据库：打开mysql命令程序用sql语句新建即可，此处不做详细说明。

   相关数据填充：在training项目：conf/create.sql路径文件下有着相关sql语句，如图：

   [![oJH5kV.png](https://z3.ax1x.com/2021/12/01/oJH5kV.png)](https://imgtu.com/i/oJH5kV)

​      将文件内所有sql语句复制粘贴到sql命令程序运行，即可拥有完整的training数据，数据结构如图所示：

   [![oJHhT0.png](https://z3.ax1x.com/2021/12/01/oJHhT0.png)](https://imgtu.com/i/oJHhT0)

5. 现在项目的运行环境与数据库的数据支持均已完成，我们点击IDE右上角Application右边启动按钮，或在edu/cust/Application.java中，点击main方法左边按钮，启动运行程序。

   [![oJHfwq.png](https://z3.ax1x.com/2021/12/01/oJHfwq.png)](https://imgtu.com/i/oJHfwq)

6. 当控制台显示如下日志，则代表项目成功完美运行：

   ![RNJQ_CS_BYJ5G9ZDK_BS~36.png](https://i.loli.net/2021/12/01/TvNSHJCxfyBPQmc.png)

### 访问与登录

打开浏览器访问如下路径：localhost:8080/training/adm/index.jsp

[![oJHWmn.png](https://z3.ax1x.com/2021/12/01/oJHWmn.png)](https://imgtu.com/i/oJHWmn)

输入账号:111；密码:111。登录成功!

[![oJb5DA.png](https://z3.ax1x.com/2021/12/01/oJb5DA.png)](https://imgtu.com/i/oJb5DA)

## training各个模块的作用

### 后端

​	[![oJb4ud.png](https://z3.ax1x.com/2021/12/01/oJb4ud.png)

补充: application.properties：应用配置文件

​		 applicationContext.xml：文件配置(侧重于bean)

### 前端

[![oJOC1U.png](https://z3.ax1x.com/2021/12/01/oJOC1U.png)](https://imgtu.com/i/oJOC1U)

## 初步实现增删改查

模仿框架中的用户管理，做一个学生信息管理界面出现在框架中。

逻辑思路:

1. 在数据库中增加你想要的数据
2. 在项目中构建对应的DAO层和实体类
3. 前端创建对应的页面，并加入到主页中
4. 后端创建xxxAction.java的文件，通过requestMapping等注解与前端页面进行数据的相互传输

### 操作范例

以下为演示步骤仅供参考，具体以实际需求为基准

#### 数据库填充数据

创建数据表并填充数据，这里以实验室成员名单构建了一张数据表。

[![oaFrIP.png](https://z3.ax1x.com/2021/12/03/oaFrIP.png)](https://imgtu.com/i/oaFrIP)

#### 创建DAO层及实体类

​	在src/main/java/edu/cust路径下新建student 包，包内部新建目录及文件如下图:

[![ottKLq.png](https://z3.ax1x.com/2021/12/02/ottKLq.png)](https://imgtu.com/i/ottKLq)

在StudentDAO层构建如下代码：

```java
@Component
public class StudentDAO extends DAOTemplate<Student> {
    public StudentDAO(){
        //反射的知识点，用来动态装配实体类
        clazz = Student.class;
        //数据表的主键
        pkColumns = new String[]{"c_number"};
        //列出所需要的字段(若不赋值该变量则默认所有字段都可列出)
        listProjections = new String[]{"c_number", "c_name","c_grade"};
        //要操控的字段
        comColumns = new String[]{"c_name","c_grade"};
        //数据表
        tableName = "c_student";
        init();
    }
}
```

实体类如下:

```java
/**
 * student的实体类，与DAO层相关的字段及主键自动赋值给实体类
 * 命名方式采用数据表字段去掉c_前缀，当遇见 _ 时第二个字母大写，如：   c_name => name,c_abc_name => abcName
 * 严格采用上述方法命名，否则将导致程序异常
 * 变量尽量做好注释
 * 
 * created by ybl at 2021/12/2
 */

//Data注解用于给实体类自动创建getter/setter等实体类常用方法
@Data
public class Student {
    //学号
    private String number;

    //姓名
    private String name;
    //年级
    private int grade;
}

```

#### 前端创建对应页面

​	在src/main/webapp/adm下创建students包，新建listStudents.jsp：

[![otBLPP.png](https://z3.ax1x.com/2021/12/02/otBLPP.png)](https://imgtu.com/i/otBLPP)

在index.jsp中插入一下代码让其显示(在哪插入很容易知道)

```html
<li><a href="#a" data-toggle="tab"
  data-src="students/listStudents.jsp">
    <span  class="glyphicon glyphicon-user"></span>
    学生管理
    </a></li>
```

重新运行项目，访问主页，效果如下:

[![oUfz4O.png](https://z3.ax1x.com/2021/12/03/oUfz4O.png)](https://imgtu.com/i/oUfz4O)

#### 实现前后端数据交互

​	代码逻辑和页面布局我们仿造用户管理,在此基础上做修改即可(不重复造轮子)。这里给出参考代码:

##### 前端

前端涉及到基础html的编写与运用，如果看不懂请对html及jquery进行初步入门的学习。

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--jsp中jstl的使用,获取该页面所在前端文件夹根目录--%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <title>学生管理管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../../y/css/bootstrap.min.css">
    <link rel="stylesheet" href="../../y/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="../../y/css/common.css">
    <script type="text/javascript" src="../../y/js/jquery.min.js"></script>
    <script type="text/javascript" src="../../y/js/jquery.form.min.js"></script>
    <script type="text/javascript" src="../../y/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="../../y/js/jquery.placeholder.min.js"></script>
    <script type="text/javascript" src="../../y/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../../y/js/vue.min.js"></script>
    <script type="text/javascript" src="../../y/js/common.js"></script>
    <script type="text/javascript">
        var vum;
        var page = 1;

        $(document).ready(function() {
            //创建一个vue实例，对id为app的图层进行渲染
            vum = new Vue({
                el : "#app",
                data : {
                    datas : [],
                    pages : []
                },
                methods : {
                    showData : function(p) {
                        page = p;
                        search(p,'listAjax','searchForm', vum);
                    }
                }
            });
            vum.showData(page);

            $("#addForm").validate({
                submitHandler : function(form) {
                    $(form).ajaxSubmit({
                        dataType : "json",
                        success : function(data) {
                            alert(data.retMsg);
                            $("#addModal").modal("hide");
                            vum.showData(page);
                        },
                        error : function(data) {
                            alert(data.status);
                        }
                    });
                }
            });

            $("#importForm").validate({
                submitHandler : function(form) {
                    var btn = loading($("#importForm input[type='submit']"))
                    if(btn.isloading){
                        return;
                    }
                    $(form).ajaxSubmit({
                        dataType : "json",
                        success : function(data) {
                            alert(data.retMsg);
                            $("#importModal").modal("hide");
                            vum.showData(page);
                            unloading(btn);
                        },
                        error : function(data) {
                            alert(data.status);
                            unloading(btn);
                        }
                    });
                }
            });

        });

        function add() {
            fillForm({});
            $('#number').attr("readOnly", false);
            $('#addForm').attr("action", "addAjax");
            $('#addModalLabel').text("添加学生信息");
            $('#addModal').modal("toggle");
        }

        function del(id) {
            if (confirm("确定删除该学生吗？")) {
                $.ajax({
                    url : "deleteAjax",
                    type : "POST",
                    data : {
                        number : id
                    },
                    success : function(data) {
                        alert(data.retMsg);
                        vum.showData(page);
                    }
                });
            }
        }

        function openImportForm() {
            $('#importForm').attr("action", "importAjax");
            $('#importModalLabel').text("导入学生");
            $('#importModal').modal("toggle");
        }

        function load(data) {
            $('#number').attr("readOnly", true);
            $('#addForm').attr("action", "updateAjax");
            $('#addModalLabel').text("修改学生信息");
            $('#addModal').modal("toggle");
            fillForm(data);
        }

        function fillForm(obj) {
            $("#addForm input[type='text']," +
                "#addForm input[type='number']").val(function(index, value) {
                return obj[this.id];
            });
        }
    </script>
</head>
<body>
<div class="container-fluid" align="center">
    <div class="form-group" style="text-align: left">
        <form id="searchForm">
            <input type="hidden" name="columns" value="c_number">
            <input type="hidden" name="columns" value="c_name">
            <input type="hidden" name="operators" value="like">
            <input type="hidden" name="operators" value="like">
            <input type="hidden" name="orders" value="none">
            <input type="hidden" name="orders" value="none">
            <input type="hidden" name="logicalopts" value="">
            <input type="hidden" name="logicalopts" value="and">
            <span class="col-md-2">
				<input type="text" name="values" class="form-control" placeholder="学号">
				</span>
            <span class="col-md-2">
				<input type="text" name="values" class="form-control" placeholder="姓名">
				</span>
        </form>
        <button class="btn btn-primary"
                onClick="search(1,'listAjax','searchForm', vum);">
            <span class="glyphicon glyphicon-search"></span>
        </button>
        <button class="btn btn-primary" onClick="add();">添加学生</button>
    </div>
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog"
         aria-labelledby="addModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="addModalLabel">添加学生</h4>
                </div>
                <form id="addForm" method="post" action="" class="form-horizontal">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="number" class="col-sm-3 control-label">学号</label>
                            <div class="col-sm-9">
                                <input type="text" name="number" id="number" class="form-control"
                                       placeholder="请输入学号">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-3 control-label">姓名</label>
                            <div class="col-sm-9">
                                <input type="text" name="name" id="name" class="form-control"
                                       placeholder="请输入学生姓名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="grade" class="col-sm-3 control-label">年级</label>
                            <div class="col-sm-9">
                                <input type="number" name="grade" id="grade" class="form-control"
                                       placeholder="请输入年级">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default"
                                data-dismiss="modal">关闭</button>
                        <input type="submit" class="btn btn-primary" value="提交">
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%--该图层为vue渲染的图层，具有vue.js特性和关键语句，请阅读vue.js官方文档进行初步入门的学习--%>
    <div id="app">
        <table class="table table-hover">
            <tr>
                <th>学号</th>
                <th>姓名</th>
                <th>年级</th>
            </tr>
            <tr v-for="data in datas">
                <td>{{data.number}}</td>
                <td>{{data.name}}</td>
                <td>{{data.grade}}</td>
                <td><button title="编辑学生信息"
                            @click="load(data)">
                    <span class="glyphicon glyphicon-edit"></span>
                </button>
                    <button title="删除用户" @click="del(data.number);">
                        <span class="glyphicon glyphicon-remove"></span>
                    </button>
                </td>
        </table>
        <span>总数量：{{pages.rowCount}}</span> <span>总页数：{{pages.lastPage}}</span>
        <button class="btn btn-default btn-sm"
                @click="showData(pages.firstPage)">
            <span class="glyphicon glyphicon-backward"></span>
        </button>
        <button class="btn btn-default btn-sm"
                @click="showData(pages.prePage)">
            <span class="glyphicon glyphicon-chevron-left"></span>
        </button>
        <span>当前页：{{pages.pageNum}}</span>
        <button class="btn btn-default btn-sm"
                @click="showData(pages.nextPage)">
            <span class="glyphicon glyphicon-chevron-right"></span>
        </button>
        <button class="btn btn-default btn-sm"
                @click="showData(pages.lastPage)">
            <span class="glyphicon glyphicon-forward"></span>
        </button>
    </div>
</div>
</body>
</html>
```

##### 后端

请仔细阅读以下代码和注释，分析其功能，运用IDE的断点调试了解其实现原理。

```java

/**
 * 学生管理的action，对前后端进行数据的交互
 * 其中@Controller注解声明了这是Controller层，也为@Autowired
 * 注解提供了注入条件
 *
 * 而@Scope注解解决跨域问题
 * created by ybl at 2021/12/3
 */
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StudentAction extends AbstractController {
    //使用@Autowired使数据库连接等配置从容器中自动依赖注入到以下全局变量
    @Autowired
    private JdbcTemplate jt;

    @Autowired
    private StudentDAO studentDAO;

//    @RequiresPermissions({"yhgl"}) 定义接口访问权限，这里默认每个人都能访问，故不进行设置
    //RequestMapping注解接受后端请求并进行相应
    @RequestMapping("/adm/students/listAjax")
    public String list(int rows, int page, Search search, Model model) {
        //用search对象中的buildSQL方法构建前端所需要的sql查询语句
        String sql = search.buildSQL(studentDAO);
        sql += " order by c_number asc";
        //获取前端传递的分页数据配置 page: 当前所在页；rows:前端设定的一页多少行数据
        Page mlpage = PageFactory.getPage();
        mlpage.setPageNum(page);
        mlpage.setRecordNum(rows);
        List<Object> params = search.getParams();
        //params.add(cp);
        //根据sql语句与当前分页配置检索出该页数据
        List<?> result = mlpage.getOnePage(sql, params, studentDAO);
        //model返回给后端
        model.addAttribute("pages", mlpage);
        model.addAttribute("result", result);
        return "json";
    }

    //@Transactional注解声明改server会操作修改数据库中数据
    @Transactional
//    @RequiresPermissions({"yhgl"})
    @RequestMapping("/adm/students/deleteAjax")
    public String delete(String number, Model model) {
        //用封装好的方法对数据库进行删除指定行的操作(主键为索引)
        studentDAO.delete(number);
        model.addAttribute("retMsg", "删除成功");
        return "json";
    }

    @Transactional
    @RequestMapping("/adm/students/addAjax")
    public String add(Student student, Model model) {
        //用封装好的方法对数据库进行添加操作(实体类的值自动装配)
        studentDAO.insert(student);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "添加成功");
        return "json";
    }

    @Transactional
    @RequestMapping("/adm/students/updateAjax")
    public String update(Model model, Student student) {
        ////用封装好的方法对数据库进行查询操作(主键为索引,实体类的值自动装配)
        Student a = studentDAO.loadOne(student.getNumber());
        a.setName(student.getName());
        a.setGrade(student.getGrade());
        studentDAO.update(a);
        model.addAttribute("retCode", "OK");
        model.addAttribute("retMsg", "更新成功");
        return "json";
    }

    //注：该方法未被使用过
    @RequestMapping("/adm/students/existAjax")
    //该注解将返回值作为http正文返回给前端
    @ResponseBody
    public boolean existAjax(String number, Model model) {
        //判断是否存在该学生
        Student student = studentDAO.loadOne(number);
        return student == null;
    }
}

```

##### 最终成果

​	成功！

[![oaFpvQ.png](https://z3.ax1x.com/2021/12/03/oaFpvQ.png)](https://imgtu.com/i/oaFpvQ)

## 开发项目时的建议

- 养成查询资料精确搜索能力：开发遇见问题，项目抛出未知异常，对某函数功能不熟悉时，正确使用搜索引擎查询关键字，精确锁定问题。提高自己精确搜索能力。
- 优先观看官方文档: 如遇见不熟悉的前端框架组件或后端api，首先查看官方中文文档，如果一知半解，再在网上论坛搜索提问。
- 养成优秀代码习惯：写代码时注意代码基本规范，如java变量命名采用驼峰命名法，类之间注意耦合和内聚性，相同代码段不宜过多等，具体可参考**阿里巴巴代码规范**，如不够明白，可使用[java开发规约IDE插件](https://github.com/alibaba/p3c)，在IDE中导入使用。
- 边实践边学习：编程语言仅是一个工具，在项目开发中，重要的是编程思想的建立和思考逻辑的清晰，当对一个工具有初步的了解与掌握后，快速实践上手积累经验比系统理论学习更加高效，反之，一直学习不注重实际运用，项目开发时的效率并不比边做边学快，遇到了问题也并不比其少，甚至会延迟项目完成时间。

---

​	author：web组 易俊

​	最后更新时间: 2021/12/3
