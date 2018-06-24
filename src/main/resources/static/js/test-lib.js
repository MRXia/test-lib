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
            questions:[]
        },
        stat = {
            current : 0,    // 当前题号
            score : 0,      // 总分
            selected : [],  // 已选情况
        },
        question = $("#questionContent"),
        checked = $('<span class="oi oi-check"></span>');

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
                selectQuestion(0);
            });
        },

        countdown = function (time) {

        },

        // 选择答案
        chooseAnswer = function (answer) {
            stat.selected[stat.current] = answer;
            checkedOption(answer);
        },

        // 选择考题
        selectQuestion = function (qid) {
            stat.current = qid;
            question.html(template("questionTpl", {
                question: paper.questions[qid],
                current: qid,
                isLast: qid === paper.questions.length - 1
            }));

            let answer = stat.selected[qid];
            if (answer) {
                checkedOption(answer);
            }
        },

        checkedOption = function (answer) {
            question.find(".list-group .list-group-item").children().remove();
            $("#answer" + answer).append(checked);
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