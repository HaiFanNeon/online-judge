CREATE DATABASE oj_dev DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
use oj_dev;
# 账号密码 字符串类型

# 表明小写 多个单词 下划线隔开 以tb开头
# 保证查询效率和账号唯一性，可以创建一个唯一索引
# 追钟数据创建，修改的历史，寻找责任人
create table tb_sys_user(
    user_id      bigint      unsigned not null comment '用户id(主键)',
    user_account varchar(20) not null comment '账号',
    password     varchar(20) not null comment '密码',
    create_by    bigint      unsigned not null comment '创建人',
    create_time  datetime    not null comment '创建时间',
    update_by    bigint      unsigned comment '更新人',
    update_time  datetime             comment '更新时间',
    primary key (`user_id`),
    unique key `idx_user_account` (`user_account`)
);

create table tb_question(
    question_id         bigint          unsigned not null comment '题目id',
    title               varchar(50)     not null  comment '题目标题',
    difficulty          tinyint         not null comment '题目难度1:简单  2：中等 3：困难',
    time_limit          int             not null comment '时间限制',
    space_limit         int             not null comment '空间限制',
    content             varchar(1000)   not null comment '题目内容',
    question_case       varchar(1000)   comment '题目用例',
    default_code        varchar(500)    not null comment '默认代码块',
    main_fuc            varchar(500)    not null comment 'main函数',
    create_by           bigint          unsigned not null  comment '创建人',
    create_time         datetime        not null comment '创建时间',
    update_by           bigint          unsigned  comment '更新人',
    update_time         datetime        comment '更新时间',
    primary key(`question_id`)
);