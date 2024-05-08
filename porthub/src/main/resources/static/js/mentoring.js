
function mentorpopup(e){
 $('#mentoringpostid').attr('value',e.id)
 $.ajax({
  type: "post",
  url: "/mentoring/load",
  data:{
   MentoringID:e.id
  },
  dataType:"json",
  success: function (data) {
   $('.mentoimg').attr('src',data.profileImage)
   $('.mentoname').text(data.MentoName);
   $('.postname').text(data.Title);
   $('.post-con').html(data.Contents);
   $('.mentoprice').text(data.Price)

  },
  error: function (err) {
   console.log("error", err);
  },
 })
 $('.layer1').css('display','block');


}

$(function(){
 $('.close').click(function(){
  $('.layer1').css('display','none');
 })
})

$(function(){
 $('#searchbutton').click(function(){
  $.ajax({
   type: "post",
   url: "/mentoring/search",
   data:{
    searchString:$('#searchbox').val()
   },
   dataType:"json",
   success: function (data) {
    console.log(data)
    if($.isEmptyObject(data)){
     $('.cards-section').empty();
     $('.cards-section').append('<div>검색된 결과가 없습니다.</div>')
    }
    else{
     $('.cards-section').empty();
     data.forEach((searchdata)=>{
      $('.cards-section').append('<div id="'+searchdata.mentoringID+'" onclick="mentorpopup(this)" class="mento-wrapper pop">\n' +
          '                            <div class="port-2">\n' +
          '                                <div class="title-description">\n' +
          '                                    <div class="text-wrapper-5">\n' +
          '                                        <img class="image" src="'+searchdata.thumbnail+'"/>\n' +
          '                                        <div>'+searchdata.title+'</div>\n' +
          '                                        <div class="text-wrapper-6"><img class="profile-image-sm" src="'+searchdata.profileImage+'"/><span>'+searchdata.userName+'</span></div>\n' +
          '                                        <div class="price">'+searchdata.price+'</div>\n' +
          '                                    </div>\n' +
          '                                </div>\n' +
          '                            </div>\n' +
          '                        </div>');
     })
    }
   },
   error: function (err) {
    console.log("error", err);
   },
  })
 })
})