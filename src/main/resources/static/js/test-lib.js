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

    let paper = {
            questions:[],
            types:[]        // 保存题目类型的索引
        },
        stat = {
            current: 0,    // 当前题号
            question: {},  // 当前题目
            score: 0,      // 总分
            submitted: false //是否已交卷
        },
        question = $("#questionContent"),   // 题目面板
        qidSelector = $("#qidSelector"),    // 题目选择器
        timer,
        score = $("#score")             // 总得分
    ;


    let questionTypeEnum = [
        questionType.SingleChoiceQuestion,
        questionType.MultipleChoiceQuestion,
        questionType.TrueOrFalseQuestion,
        questionType.OperateQuestion
    ];

    let init = function (subjectId, paperId) {
        apiRequest("/paper/start",
            {
                subjectId: subjectId,
                paperId: paperId
            },
            "POST")
            .then(function (data) {
                $.extend(paper, data);
                for (let i = 0; i < data.questions.length; i++) {
                    let question = data.questions[i];
                    data.questions[i] = new questionTypeEnum[question.type](question);

                    let types = paper.types[question.type];
                    if(!types) {
                        types = [];
                        paper.types[question.type] = types;
                    }
                    types.push(i);
                }
                // 开始计时
                countdown(data.testTime)
            })
            .then(function () {
                qidSelector.find(".modal-body").html(template("qidSelectorTpl",{
                    types:paper.types
                }));
                selectQuestion(0);
            });
        },

        countdown = function (time) {
           timer = new Timer(time, "#timer span", submit);
           timer.start();
        },

        // 选择答案
        chooseAnswer = function () {

            // 如果已经交卷，则不可选择
            if(stat.submitted) {
                return;
            }

            let args = Array.prototype.slice.call(arguments);
            stat.question.chooseAnswer(...args);
        },

        // 选择考题
        selectQuestion = function (qid) {
            stat.current = qid;
            stat.question = paper.questions[qid];
            question.html(stat.question.createQuestion(qid, paper.questions.length));

            if (stat.question.selected) {
                stat.question.checkedOption(stat.question.selected);
            }

            // 如果已经交卷，则展示正确答案与提示
            if (stat.submitted) {
                showTip();
            }
        },

        showTip = function () {
            question.find(".card-footer").before(template("tipTpl",{tip: stat.question.tip}));
            stat.question.showRightAnswer();
        },

        /*
         * 计算总得分，并在展示，返回已选的答案
         */
        calScore = function () {

            let total = 0,
                selectedAnswer = [],
                getTableClass = function(correct, score) {
                if (correct) {
                    return "table-success";
                } else if (score > 0) {
                    return "table-warning";
                } else {
                    return "table-danger"
                }
            };

            paper.questions.forEach((question, index) => {
                let score = question.calScore();
                let correct = question.isCorrect();

                getTableClass(correct, score);

                qidSelector.find('.modal-body #selector' + index).addClass(getTableClass(correct, score));

                total += score;
                selectedAnswer.push({
                    type: question.type,
                    selected: question.selected,
                    correct: correct
                });
            });

            alert("总分:" + total);
            score.find('span').text(total);

            return selectedAnswer;
        },

        submit = function () {

            // 如果已经交卷，则不做处理
            if(stat.submitted) {
                return;
            }

            stat.submitted = true;
            $("#submit").hide();
            $("#return").show();

            // 停止计时
            timer.stop();

            // 计算总分
            console.log(calScore());

            // 展示提示与正确答案
            showTip();
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