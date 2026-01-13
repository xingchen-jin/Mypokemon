# Jade-Journey / 宝石之旅
Designed by JinJiaWei , ZUST_CS241, 12406960344

简介 / Overview
----------------
- 中文：独立完成的大学课程设计，在三周内复刻了《宝可梦 绿宝石》核心体验，涵盖输入、实时渲染、碰撞、资源管理与回合制战斗；架构清晰、模块化，便于学习与扩展。
- English: A three-week university course project recreating the core experience of Pokémon Emerald. Covers input, rendering, collision, resource management, and turn-based battles with a clear, modular architecture for learning and extension.

核心亮点 / Highlights
---------------------
- 约 4,300+ 行 Java 代码，单线程游戏循环，键盘全局驱动。
- 自研 2D 像素管线：瓦片地图、精灵动画、基础碰撞、对话/菜单 UI。
- 回合制战斗框架：技能、属性数据、基础伤害流程与状态管理。
- 资源加载与分层：地图/角色/精灵素材、音频与字体分目录管理。

架构与模块 / Architecture & Modules
-----------------------------------
- 游戏主循环与场景： [src/main/GamePanel.java](src/main/GamePanel.java), [src/main/Main.java](src/main/Main.java)
- 输入与事件： [src/main/KeyHandler.java](src/main/KeyHandler.java), [src/main/EventHandler.java](src/main/EventHandler.java)
- 瓦片与碰撞： [src/tile/TileManger.java](src/tile/TileManger.java), [src/main/CollisionChecker.java](src/main/CollisionChecker.java)
- 实体与角色： [src/entity/Player.java](src/entity/Player.java), [src/entity/Entity.java](src/entity/Entity.java)
- 战斗系统： [src/battle/BattleManger.java](src/battle/BattleManger.java), [src/battle/BattleSystem.java](src/battle/BattleSystem.java), [src/move](src/move)
- 精灵数据： [src/pokemon/Pokemon.java](src/pokemon/Pokemon.java) 及子类 (Mudkip, Torchic, Treecko, Ralts, Poochyena)
- 道具与背包： [src/item](src/item), [src/inventory/PokemonParty.java](src/inventory/PokemonParty.java)
- 资源与工具： [src/utils/UtilityTool.java](src/utils/UtilityTool.java), [res](res)（地图、角色、音频、字体等）

状态 / Status
-------------
- 中文：可运行的教学/同人原型，欢迎继续迭代与二次开发。
- English: Playable teaching/fan-made prototype; open to further iteration and extensions.

操作 / Controls
---------------
- W / A / S / D：控制角色与菜单指针
- ENTER：确认、交互、播放钓鱼动画
- I：切换调试模式
- ESC：返回或打开菜单

快速开始 / Getting Started
--------------------------
- 依赖 / Prerequisites: JDK 8+ 建议 / JDK 8+ recommended.
- 构建与运行（示例）/ Build & Run (example):

```powershell
# 编译示例：按需调整源路径与包名
javac -encoding UTF-8 -d bin -cp src src/main/Main.java

# 运行示例：若主类包名为 main 则如下
java -cp bin main.Main
```

功能 / Features
---------------
- 2D 像素回合制框架：单线程游戏循环，输入驱动的状态切换。
- 地图与碰撞：瓦片地图、碰撞体积、事件矩形（对话、交互触发）。
- 战斗与技能：基础回合制流程、技能数据类、属性与伤害计算骨架。
- 背包与道具：精灵队伍管理、基础道具定义与获取。
- UI 与资源：文本窗口、菜单、音频播放、精灵/角色/地图素材加载。
- 可扩展性：易于添加新地图、技能、道具、精灵与 UI 界面。

系统摘要 / Systems
------------------
- 战斗：管理战斗状态、技能释放、HP 结算与基础效果呈现。
- 移动与交互：按键驱动角色移动，事件矩形触发对话/交互逻辑。
- 资源加载：分目录管理地图 CSV、精灵帧、音频与字体，启动时按需加载。
- UI/UX：文本窗口、菜单导航、战斗界面与提示层。
- 调试支持：`I` 切换调试模式，便于查看坐标与碰撞。

目录结构（示例）/ Project Structure (example)
-------------------------------------------
- `src/`：按模块组织（main, battle, pokemon, item 等）
- `res/`：图片、音频、地图等资源
- `bin/`：编译输出

许可 / License
---------------
- 中文：本项目代码以 MIT 许可证发布，详见 [LICENSE](LICENSE)。
- English: Code is released under the MIT License. See [LICENSE](LICENSE).

版权声明（美术资源）/ Artwork Copyright
--------------------------------------
- 中文：美术资源的版权与商标权归属任天堂（Nintendo）、宝可梦公司（The Pokémon Company）、柚子社（Yuzusoft，柚子社）或其他商业游戏公司。素材仅用于学习、研究与非商业展示，不构成再分发或商业授权；如权利方要求，将及时移除相关资源或链接。
- English: Artwork assets remain the intellectual property and trademarks of Nintendo, The Pokémon Company, Yuzusoft, or other commercial game companies. Assets are used solely for learning/research/non-commercial demonstration and do not grant redistribution or commercial rights; upon request by rights holders, related assets or links will be removed promptly.

无关与免责声明 / Non-Affiliation & Disclaimer
-------------------------------------------
- 中文：Jade-Journey 为独立、非商业的学习/同人项目，与任天堂、宝可梦公司、Game Freak、Creatures Inc.、柚子社或任何其他商业游戏公司及其关联方无隶属、合作、授权或背书关系。
- English: Jade-Journey is an independent, non-commercial learning/fan-made project and is not affiliated with, endorsed by, or sponsored by Nintendo, The Pokémon Company, Game Freak, Creatures Inc., Yuzusoft, or any other commercial game companies or their affiliates.

致谢 / Acknowledgements
----------------------
- 中文：感谢开源社区、玩家与开发者的反馈和工具支持。
- English: Thanks to the open-source community, players, and developers for resources, tools, and feedback.





