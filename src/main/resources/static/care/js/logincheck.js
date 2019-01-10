var now;
var accessToken;
var accessTokenExpiredAt;
var refreshToken;
var refreshTokenExpiredAt;
var serverNow;


var initCookie = function() {
    if (!Date.now) {
        Date.now = function () {
            return new Date().getTime();
        }
    }
    console.log("현재 local time : " + Date.now());

    // var now = Math.floor(Date.now() / 1000);
    this.accessToken = $.cookie("accessToken");
    this.accessTokenExpiredAt = $.cookie("accessTokenExpiredAt");

    this.refreshToken = $.cookie("refreshToken");
    this.refreshTokenExpiredAt = $.cookie("refreshTokenExpiredAt");

    this.serverNow = $.cookie("serverNow");

}

initCookie();

var doRefreshRequest = function () {
    var headers = {};
    headers["Authorization"] = refreshToken;
    $.ajax({
        type: "POST",
        headers: headers,
        url: "/api/care/auth/refresh",
        timeout: 600000,
        success: function (res) {
            alert("토큰 갱신 완료");
            if (res.status == 201) {
                //갱신코드
                $.cookie("accessToken", res.data.data);
                $.cookie("accessTokenExpiredAt", res.data.expiredAt);
                initCookie();

                alert("accessToken 갱신 완료");
            } else {
                alert("액세스토큰 갱신 실패");
                swal({
                    title: "Server error",
                    text: "에러가 발생했습니다 다시 입력해주세요",
                    type: "error"
                });
            }
        }
    });
}

if ($.cookie("refreshToken") !== undefined) { // 리프레시 토큰이 있다
    if (this.now < this.refreshTokenExpiredAt) { // 리프레시토큰이 만료되지 않았다
        //아무것도 안해도됨
        if (this.accessTokenExpiredAt < now) { // 엑세스토큰이 만료되었다
            alert("accessToken 만료");

            doRefreshRequest();
        } else {
        }
    }
} else {
    location.href="/care/login.html";
}

//리프레시 토큰이 없다





