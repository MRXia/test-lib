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
        }

        createData(question) {
            return question;
        }

        createQuestion(question, index) {
            return template(this.tplId, {
                question:this.createData(question),
                current: index,
                isLast: index === paper.questions.length - 1
            });
        }

        chooseAnswer(answer) {
            this.checkedAnswer(answer);
            this.checkedOption(answer);
        }

        // 选择答案赋值操作
        checkedAnswer(answer) {
            stat.selected[stat.current] = answer;
        }

        // 选择答案视图操作
        checkedOption(answer) {
        }
    }

    /*单选题类型*/
    class SingleChoiceQuestion extends QuestionType {
        constructor() {
            super("singleChoiceTpl")
        }

        checkedOption(answer) {
            $("#answer" + answer).append(checked);
        }
    }

    /*多选题类型*/
    class MultipleChoiceQuestion extends QuestionType {
        constructor() {
            super("multipleChoiceTpl");
            this.answerArray = ['A', 'B', 'C', 'D'];
        }

        checkedAnswer(answer) {
            let currentAnswer = stat.selected[stat.current] || '';

            // 遍历A,B,C,D选项，如果当前答案包含而被选中，则去除选项，如果当前答案未包含而选中，则添加该选项
            currentAnswer = this.answerArray.map(value => {
                let choose = value === answer;
                if (currentAnswer.indexOf(value) === -1) {
                    return choose ? value : '';
                } else {
                    return choose ? '' : value;
                }
            }).join('');

            stat.selected[stat.current] = currentAnswer;
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
            super("trueFalseTpl")
        }

        checkedOption(answer) {
            $("#answer" + (answer === '正确' ? 'True' : 'False')).append(checked);
        }
    }

    /*操作题类型*/
    class OperateQuestion extends QuestionType {
        constructor() {
            super("operateTpl")
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
        chooseAnswer = function (answer) {
            questionTypeEnum[paper.questions[stat.current].type].chooseAnswer(answer);
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
        }
    ;

    return {
        "init": init,
        "selectQuestion": selectQuestion,
        "chooseAnswer": chooseAnswer
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