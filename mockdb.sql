
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_http_mockcaseinfo
-- ----------------------------
DROP TABLE IF EXISTS `tb_http_mockcaseinfo`;
CREATE TABLE `tb_http_mockcaseinfo` (
  `caseid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用例的id，从用例文件中获取（caseid字段），主键（索引），不能为空',
  `caseName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例名称',
  `cmdcode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口中请求业务标识码',
  `productName` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '被测试产品名称',
  `respMsg` varchar(10240) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'Mock返回给被测试App的内容',
  `matchid` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '识别请求的id',
  `respMD5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'mock报文的MD5值，用于判断respMsg值是否发生变化',
  `createDate` char(20) DEFAULT NULL COMMENT '用例生成日期',
  `updateTime` char(20) DEFAULT NULL COMMENT '用例更新日期',
  PRIMARY KEY (`caseid`),
  UNIQUE KEY `idx_tb_mockcaseinfo_caseid` (`caseid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_http_mockcaseinfo
-- ----------------------------
INSERT INTO `tb_http_mockcaseinfo` VALUES ('c3af88a6-7367-4d40-9363-d20ca9c83490', '每日推荐-推荐名称字段为其它任意名称', '0265001', '秋天的风景', '{\"cmdCode\":\"0167001\",\"resultCode\":\"200\",\"resultMessage\":\"成功！\",\"resultObject\":{\"image\":\"http://180.96.8.92/strategyservice/image/showByName.htm?name=S_xwb001_20190130085339_455_size0.png\",\"stocks\":[{\"bright\":\"关于MD5加密\",\"hisFlag\":\"0\",\"id\":4207,\"increase\":\"-3.16%\",\"inputDate\":1548809910000,\"price\":16.22,\"NickCode\":\"9527.OP\",\"NickName\":\"MD5加密\",\"subjectId\":1086},{\"hisFlag\":\"1\",\"id\":4212,\"increase\":\"15.14%\",\"inputDate\":1548086400000,\"price\":10.15,\"NickCode\":\"300096.SZ\",\"NickName\":\"你好小风\",\"subjectId\":1086}],\"subject\":\"朋友们，大家好\",\"title\":\"每日金股\"},\"returnTime\":1548838215134,\"success\":true}', '2001', 'c045bb3733f9668aaa5c56c3785e2455', '2019-02-20 10:07:57', '2019-02-20 10:07:57');
INSERT INTO `tb_http_mockcaseinfo` VALUES ('d6a835cd-bc48-4053-9ab5-3af3b864def9', '每日推荐-推荐名称字段为数字', '0265001', '每日新闻(DailyNews)', '{\"cmdCode\":\"0167001\",\"resultCode\":\"200\",\"resultMessage\":\"成功！\",\"resultObject\":{\"image\":\"http://180.96.8.92/strategyservice/image/showByName.htm?name=S_xwb001_20190220082331_245_size0.png\",\"stocks\":[{\"bright\":\"红发姑娘\",\"hisFlag\":\"0\",\"id\":4267,\"increase\":\"9999.98%\",\"inputDate\":1550622443000,\"price\":26.03,\"NickCode\":\"10086AZ\",\"NickName\":\"雪中的柑橘\",\"subjectId\":1097},{\"bright\":\"红发姑娘\",\"hisFlag\":\"0\",\"id\":4268,\"increase\":\"9999.98%\",\"inputDate\":1550622443000,\"price\":46.88,\"NickCode\":\"300685.SZ\",\"NickName\":\"雪中的柑橘\",\"subjectId\":1097},{\"bright\":\"红发姑娘\",\"hisFlag\":\"0\",\"id\":4269,\"increase\":\"9999.98%\",\"inputDate\":1550622443000,\"price\":20.35,\"NickCode\":\"300348.SZ\",\"NickName\":\"雪中的柑橘\",\"subjectId\":1097},{\"hisFlag\":\"1\",\"id\":4270,\"increase\":\"9999.98%\",\"inputDate\":1550505600000,\"price\":4.17,\"NickCode\":\"002002.SZ\",\"NickName\":\"雪中的柑橘\",\"subjectId\":1097},{\"hisFlag\":\"1\",\"id\":4271,\"increase\":\"9999.98%\",\"inputDate\":1550419200000,\"price\":4.5,\"NickCode\":\"002249.SZ\",\"NickName\":\"雪中的柑橘\",\"subjectId\":1097},{\"hisFlag\":\"1\",\"id\":4272,\"increase\":\"9999.98%\",\"inputDate\":1550419200000,\"price\":10.85,\"NickCode\":\"603399.SH\",\"NickName\":\"雪中的柑橘\",\"subjectId\":1097}],\"subject\":\"赚钱效应开始增强\",\"title\":\"每日金股\"},\"returnTime\":1550655361817,\"success\":true}', '2001', 'be98923e3dce84d916b668d7db3a9f38', '2019-02-20 17:44:25', '2019-02-20 17:44:25');
INSERT INTO `tb_http_mockcaseinfo` VALUES ('dd424c12-1226-4c07-a6ea-b674f585b7b5', '龙虎榜-龙虎榜标题修改', '0101001', '天下事(NewsEveryday)', '{\"cmdCode\":\"0101001\",\"resultCode\":\"0\",\"resultMessage\":\"成功\",\"resultObject\":[\"20190218\",[{\"chg\":2.69,\"name\":\"柯克你好\",\"onBillNum\":16,\"seats\":\"普通席位\",\"flyCode\":\"002943.SZ\"},{\"chg\":2.9,\"name\":\"霸王你好\",\"onBillNum\":13,\"seats\":\"普通席位\",\"flyCode\":\"603739.SH\"}]],\"returnTime\":1550484390988,\"success\":true}', '1001', '52f0df1a2c1725b1e1e1023911e315c9', '2019-02-20 10:07:26', '2019-02-20 10:07:26');

-- ----------------------------
-- Table structure for tb_http_mockmatch
-- ----------------------------
DROP TABLE IF EXISTS `tb_http_mockmatch`;
CREATE TABLE `tb_http_mockmatch` (
  `matchid` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '匹配的规则id',
  `reqpath` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Http请求的url，可以是完整请求的部分路径',
  `productName` char(24) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '产品名称，值的范围为：每日新闻(DailyNews)；天下事(NewsEveryday)；无语新闻（NotingNews）',
  PRIMARY KEY (`matchid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_http_mockmatch
-- ----------------------------
INSERT INTO `tb_http_mockmatch` VALUES ('1001', 'targetservice_1/app/latestnews', '每日新闻(DailyNews)');
INSERT INTO `tb_http_mockmatch` VALUES ('2001', 'targetservice_2/app/latestnews', '天下事(NewsEveryday)');
INSERT INTO `tb_http_mockmatch` VALUES ('3001', 'targetservice_2/app/latestnews', '无语新闻（NotingNews）');
