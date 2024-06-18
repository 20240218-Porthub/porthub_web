
$(function(){
 // $(document).on("click", ".text-wrapper-6",function(e){
 //  e.stopImmediatePropagation();
 //  //location.href='profile/'+e.target.innerText
 //  e.stopPropagation();
 // })

 $('.mentoprofile').click(function(e){
  if(e.target.parentElement.className==='mentoprofile'){
   location.href='profile/'+e.target.parentElement.innerText
  }
  // location.href='profile/'+e.target.innerText
 })

 $(document).on("click",".mento-wrapper",function(e){
  e.stopImmediatePropagation();
  if(e.target.parentElement.className==='text-wrapper-6'){
   location.href='profile/'+e.target.parentElement.innerText
  }
  else{
   $.ajax({
    type: "post",
    url: "/mentoring/load",
    data:{
     MentoringID:e.currentTarget.id
    },
    dataType:"json",
    success: function (data) {
     $('.company').text(data.company)
     $('.univ').text(data.univ)
     $('.certificate').text(data.certificate)
     $('.mentoimg').attr('src',data.profileImage)
     $('.mentoname').text(data.MentoName);
     $('.postname').text(data.Title);
     $('.post-con').html(data.Contents);
     $('.mentoprice').text(data.Price);
     $('#mentoringpostid').attr('value',data.MentoringID);
     $('#locatebutton').empty()
     if(data.hasOwnProperty('MentoisMe') || data.hasOwnProperty('alreadypay')){
      if(data.MentoisMe=="Y"){
       $('#locatebutton').append('<button type="submit" form="paymentdata" class="btn btn-primary pay go-pay" disabled>Its mine</button>')
      }
      else{
       if(data.alreadypay=="Y"){
        $('#locatebutton').append('<button type="submit" form="paymentdata" class="btn btn-primary pay go-pay" disabled>이미 결제된 내역입니다.</button>')
       }else{
        $('#locatebutton').append('<button type="submit" form="paymentdata" class="btn btn-primary pay go-pay">결제하기</button>')
       }
      }
     }


    },
    error: function (err) {
     console.log("error", err);
    },
   })
   $('.layer1').css('display','block');

  }
 })
})

$(function(){
 $('.close').click(function(){
  $('.layer1').css('display','none');
 })
})

$(function() {
 $('.categories').click(function () {
  if ($(".categories").hasClass("selected")) {
   $(".selected").removeClass("selected")
  }
  CategoryName = $(this).text()
  $(this).addClass("selected")
  $.ajax({
   type: "post",
   url: "/mentoring/search",
   data:{
    "CategoryName":CategoryName
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
      $('.cards-section').append('<div id="'+searchdata.mentoringID+'" class="mento-wrapper pop">\n' +
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

$(function(){
 $('#searchbutton').click(function(){
  var searchString = $('#searchbox').val();

  var data = {};

  if (searchString) {
   data.searchString = $('#searchbox').val();
  }
  if (CategoryName) {
   data.CategoryName = CategoryName;
  }
  $.ajax({
   type: "post",
   url: "/mentoring/search",
   data:data,
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
      $('.cards-section').append('<div id="'+searchdata.mentoringID+'" class="mento-wrapper pop">\n' +
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

$(document).on('click','.layer1',function(e){
 if(!$(e.target).hasClass('layer2') && $(e.target).parents('.layer2').length===0){
  $('.layer1').css('display','none');
 }
})