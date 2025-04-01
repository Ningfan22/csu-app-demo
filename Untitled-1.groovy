import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

/// 主应用，管理全局浅色/深色主题状态
class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool _isDarkMode = false;

  void toggleTheme() {
    setState(() {
      _isDarkMode = !_isDarkMode;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '中南大学信息门户',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
        brightness: Brightness.light,
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.blue,
        ),
      ),
      darkTheme: ThemeData(
        primarySwatch: Colors.blue,
        brightness: Brightness.dark,
        appBarTheme: const AppBarTheme(
          backgroundColor: Colors.black,
        ),
      ),
      themeMode: _isDarkMode ? ThemeMode.dark : ThemeMode.light,
      home: HomePage(
        isDarkMode: _isDarkMode,
        onToggleTheme: toggleTheme,
      ),
    );
  }
}

/// 主页面，包含顶部栏、搜索栏、功能网格和校园公告
class HomePage extends StatefulWidget {
  final bool isDarkMode;
  final VoidCallback onToggleTheme;

  const HomePage({Key? key, required this.isDarkMode, required this.onToggleTheme}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  // 默认身份为学生
  bool _isStudent = true;
  final String studentId = "1916240407";

  // 功能模块列表：包含按钮名称和对应的图标
  final List<Map<String, dynamic>> functionalities = [
    {"label": "课表", "icon": Icons.calendar_today},
    {"label": "校园卡", "icon": Icons.credit_card},
    {"label": "通知", "icon": Icons.notifications},
    {"label": "成绩", "icon": Icons.grade},
    {"label": "图书馆", "icon": Icons.local_library},
    {"label": "地图", "icon": Icons.map},
    {"label": "请假申请", "icon": Icons.request_page},
    {"label": "校车时刻", "icon": Icons.directions_bus},
    {"label": "校园邮箱", "icon": Icons.email},
  ];

  // 切换身份：学生 <-> 教工
  void toggleIdentity() {
    setState(() {
      _isStudent = !_isStudent;
    });
  }

  // 点击任一功能按钮弹出“系统维护中”的提示
  void _showMaintenanceDialog(String feature) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(feature),
        content: const Text("系统维护中"),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text("确定"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    // 根据屏幕宽度决定每行显示几个功能按钮
    int crossAxisCount = MediaQuery.of(context).size.width > 600 ? 4 : 3;

    return Scaffold(
      appBar: AppBar(
        // 顶部栏包含校徽、学校名称、主题切换、身份切换、学号显示与头像
        title: Row(
          children: [
            // 校徽：使用 AssetImage，确保已在 pubspec.yaml 中配置
            CircleAvatar(
              backgroundImage: const AssetImage('assets/csu_logo.png'),
              backgroundColor: Colors.transparent,
            ),
            const SizedBox(width: 8),
            const Text("中南大学"),
          ],
        ),
        actions: [
          // 浅色/深色模式切换按钮
          IconButton(
            icon: widget.isDarkMode ? const Icon(Icons.dark_mode) : const Icon(Icons.light_mode),
            onPressed: widget.onToggleTheme,
            tooltip: "切换浅/深色模式",
          ),
          // 身份切换按钮
          TextButton(
            onPressed: toggleIdentity,
            child: Text(
              _isStudent ? "学生" : "教工",
              style: const TextStyle(color: Colors.white),
            ),
          ),
          // 学号显示
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8.0),
            child: Center(
              child: Text(
                "学号: $studentId",
                style: const TextStyle(color: Colors.white),
              ),
            ),
          ),
          // 用户头像：使用 AssetImage，确保资源文件存在
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8.0),
            child: CircleAvatar(
              backgroundImage: const AssetImage('assets/avatar.png'),
            ),
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            // 搜索栏
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: TextField(
                decoration: InputDecoration(
                  hintText: "搜索",
                  prefixIcon: const Icon(Icons.search),
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                ),
              ),
            ),
            // 功能模块区域：使用 GridView 显示各功能按钮
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: GridView.builder(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                itemCount: functionalities.length,
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: crossAxisCount,
                  mainAxisSpacing: 16,
                  crossAxisSpacing: 16,
                  childAspectRatio: 1,
                ),
                itemBuilder: (context, index) {
                  final feature = functionalities[index];
                  return ElevatedButton(
                    onPressed: () => _showMaintenanceDialog(feature["label"]),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.all(8),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(feature["icon"], size: 40),
                        const SizedBox(height: 8),
                        Text(feature["label"]),
                      ],
                    ),
                  );
                },
              ),
            ),
            const SizedBox(height: 16),
            // 校园公告区域
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Card(
                elevation: 4,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "校园公告",
                        style: Theme.of(context).textTheme.headline6,
                      ),
                      const Divider(),
                      ListTile(
                        title: const Text("网络维护通知"),
                        subtitle: const Text("近期校园网络将进行维护，部分服务可能受影响。"),
                        trailing: const Icon(Icons.chevron_right),
                        onTap: () => _showMaintenanceDialog("网络维护通知"),
                      ),
                      ListTile(
                        title: const Text("图书馆新书上架"),
                        subtitle: const Text("图书馆新增多本新书，欢迎借阅。"),
                        trailing: const Icon(Icons.chevron_right),
                        onTap: () => _showMaintenanceDialog("图书馆新书上架"),
                      ),
                      ListTile(
                        title: const Text("校内活动通知"),
                        subtitle: const Text("本周末将举行校内文艺汇演，详情请关注校园公告。"),
                        trailing: const Icon(Icons.chevron_right),
                        onTap: () => _showMaintenanceDialog("校内活动通知"),
                      ),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),
          ],
        ),
      ),
    );
  }
}
