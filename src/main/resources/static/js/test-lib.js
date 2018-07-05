const apiRequest = function (url, data, method) {
    return jQuery.ajax(url, {
        method: method || 'GET',
        contentType: 'application/json',
        data: JSON.stringify(data)
    }).then(function (response) {
        if (response.code === 200) {
            return response.data;
        }
        Alert.error(response.msg);
        return Promise.reject(response.msg);
    });
};

const testPaper = (function ($) {

    /*
     * 问题类型公共类
     */
    class QuestionType {
        constructor(tplId) {
            this.tplId = tplId;
            this.answerArray = ['A', 'B', 'C', 'D'];
        }

        // 构建题目数据
        createData(question) {
            return question;
        }

        // 使用题目数据和题号，调用对应模板，构建题目
        createQuestion(question, index) {
            return template(this.tplId, {
                question:this.createData(question),
                current: index,
                isLast: index === paper.questions.length - 1
            });
        }

        // 选择答案操作
        chooseAnswer(answer, item) {
            this.checkedAnswer(answer, item);
            this.checkedOption(answer, item);
        }

        // 选择答案赋值操作
        checkedAnswer(answer) {
            stat.selected[stat.current] = answer;
        }

        // 选择答案视图操作
        checkedOption(answer) {
        }

        /* 多选题选择答案时，根据答案列表与当前选项，返回选择后的结果
         * 遍历A,B,C,D选项，如果当前答案包含而被选中，则去除选项，如果当前答案未包含而选中，则添加该选项
         * 如已选择"AC", 若选择"B"选项，则返回"ABC";若选择"A"选项，则返回"C"
         */
        checkedMultiAnswer(answer, currentAnswer) {
            return this.answerArray.map(value => {
                let choose = value === answer;
                if (currentAnswer.indexOf(value) === -1) {
                    return choose ? value : '';
                } else {
                    return choose ? '' : value;
                }
            }).join('');
        }

        compareAnswer(currentAnswer, rightAnswer) {
            return currentAnswer === rightAnswer;
        }
    }

    /*单选题类型*/
    class SingleChoiceQuestion extends QuestionType {
        constructor() {
            super("singleChoiceTpl");
        }

        checkedOption(answer) {
            $("#answer" + answer).append(checked);
        }
    }

    /*多选题类型*/
    class MultipleChoiceQuestion extends QuestionType {
        constructor() {
            super("multipleChoiceTpl");
        }

        checkedAnswer(answer) {
            let currentAnswer = stat.selected[stat.current] || '';
            stat.selected[stat.current] = this.checkedMultiAnswer(answer, currentAnswer);
        }

        checkedOption(answer) {

            // 多选题添加选中样式时需使用checked的clone对象
            question.find(".list-group .list-group-item").children().remove();
            stat.selected[stat.current].split('').forEach(value => {
                $("#answer" + value).append(checked.clone());
            });
        }
    }

    /*判断题类型*/
    class TrueOrFalseQuestion extends QuestionType {
        constructor() {
            super("trueFalseTpl");
        }

        checkedOption(answer) {
            $("#answer" + (answer === '正确' ? 'True' : 'False')).append(checked);
        }
    }

    /*操作题类型*/
    class OperateQuestion extends QuestionType {
        constructor() {
            super("operateTpl");
        }

        createData(question) {

            // 如果操作题未被渲染，则进行预处理
            if (!question.items) {
                this.renderItems(question);
            }

            return question;
        }

        renderItems(question){

            let title = "";
            let items = [];
            let item;

            // 通过<br>切割操作题问题，进行预处理
            let itemStart = false;
            let itemStatus;

            question.title.split("<br>").forEach(value => {

                // 排除字符串为空的行
                if(!$.trim(value)) {
                    return;
                }

                // 判断小题是否开始, 如果未开始，则为大题目，拼接进title
                itemStart = itemStart || /第1小题\./.test(value);
                if (!itemStart) {
                    title += value + "<br>";
                    return;
                }

                // 处理单个选项和题目不止一行的情况，通过起始字符变换状态
                if (/第\d小题\./.test(value)) {
                    if (item) {
                        items.push(item);
                    }
                    item = {title:''};
                    itemStatus = 'title';
                } else {
                    switch ($.trim(value).substr(0, 1)) {
                        case 'A':
                            item.answerA = value;
                            itemStatus = 'answerA';
                            break;
                        case 'B':
                            item.answerB = value;
                            itemStatus = 'answerB';
                            break;
                        case 'C':
                            item.answerC = value;
                            itemStatus = 'answerC';
                            break;
                        case 'D':
                            item.answerD = value;
                            itemStatus = 'answerD';
                            break;
                        default:
                            item[itemStatus] += value;
                    }
                }
            });

            // 放入最后一题
            if (item) {
                items.push(item);
            }

            question.title = title;
            question.items = items;
        }

        checkedAnswer(answer, item) {

            if (!stat.selected[stat.current]) {
                stat.selected[stat.current] = [];
            }

            let currentAnswer = stat.selected[stat.current][item] || '';
            stat.selected[stat.current][item] = this.checkedMultiAnswer(answer, currentAnswer);
        }

        checkedOption(answer, item) {
            if(typeof(answer) === "string" && typeof(item) !== "undefined") {
                this.checkedItem(item);
            } else {
                answer.forEach((value, index) => {
                    this.checkedItem(index);
                })
            }
        }

        checkedItem(item) {
            question.find(".list-group#item" + item + " .list-group-item").children().remove();
            stat.selected[stat.current][item].split('').forEach(value => {
                $("#item" + item + "-answer" + value).append(checked.clone());
            });
        }

        compareAnswer(currentAnswer, rightAnswer) {
            rightAnswer.split()
        }
    }

    let paper = {
            questions:[]
        },
        stat = {
            current : 0,    // 当前题号
            score : 0,      // 总分
            selected : [],  // 已选情况
        },
        question = $("#questionContent"),
        qidSelector = $("#qidSelector"),
        checked = $('<span class="oi oi-check"></span>');

    let questionTypeEnum = [
        new SingleChoiceQuestion(),
        new MultipleChoiceQuestion(),
        new TrueOrFalseQuestion(),
        new OperateQuestion()
    ];

    let init = function (subjectId, paperId) {
        apiRequest("/paper/start",
            {
                subjectId: subjectId,
                paperId: paperId
            },
            "POST")
            .then(function (data) {
                paper = data;
                // 开始计时
                countdown(data.testTime)
            })
            .then(function () {
                qidSelector.find(".modal-body").html(template("qidSelectorTpl",{
                    questions:paper.questions
                }));
                selectQuestion(0);
            });
        },

        countdown = function (time) {

        },

        // 选择答案
        chooseAnswer = function () {
            let args = Array.prototype.slice.call(arguments);
            let typeEnum = questionTypeEnum[paper.questions[stat.current].type];
            typeEnum.chooseAnswer.apply(typeEnum, args);
        },

        // 选择考题
        selectQuestion = function (qid) {
            stat.current = qid;
            let questionType = questionTypeEnum[paper.questions[qid].type];
            question.html(questionType.createQuestion(paper.questions[qid], qid));

            let answer = stat.selected[qid];
            if (answer) {
                questionType.checkedOption(answer);
            }
        },

        submit = function () {

            let total = 0;
            paper.questions.forEach((question, index) => {
                let typeEnum = questionTypeEnum[question.type];
                let answer = stat.selected[index];

                if (typeEnum.compareAnswer(answer, question.rightAnswer)) {
                    total += question.score;
                }
            });

            Alert.info("总分:" + total);
        }
    ;

    return {
        "init": init,
        "selectQuestion": selectQuestion,
        "chooseAnswer": chooseAnswer,
        "submit": submit
    }

})(jQuery);

const Alert = (function ($) {

    let showMessage = function (data) {

            let alert = $(template('alertTpl', data));

            alert.insertAfter('.navbar');

            setTimeout(function () {
                alert.alert('close');
            }, 2000)

        },
        error = function (message) {
            showMessage({message: message, type: 'danger'});
        },

        warning = function (message) {
            showMessage({message: message, type: 'warning'});
        },

        success = function (message) {
            showMessage({message: message, type: 'success'});
        },

        info = function (message) {
            showMessage({message: message, type: 'info'});
        };

    return {
        success: success,
        error: error,
        warning: warning,
        info: info
    }

})(jQuery);