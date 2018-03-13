<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- global -->
<script type="text/javascript">
$(document).ready(function() {
	getUserList('','',20,1);
});

/**
 * Getting user list
 */
function getUserList(key, value, rows, page){
	$.ajax({
		 type:'GET'
		,url:'${pageContext.request.contextPath}' + '/console/user/getUserList'
		,data: {'key':key, 'value':value, 'rows':rows, 'page': page}
		,dataType:'json'
		,encode: true
		,success:function(userList) {
			console.log(userList);
			var tbody = $('#userListTable > tbody');
			tbody.empty();
			if(userList.length > 0) {
				for(var idx = 0, size = userList.length; idx < size; idx ++ ) {
					var user = userList[idx];
					var tr = $('<tr></tr>');
					tr.data('id',user.id);
					tr.click(function() {
						getUser($(this).data('id'));
					});
					tr.addClass('cursor');
					tr.append($('<td>' + user.id + '</td>'));
					tr.append($('<td>' + user.name + '</td>'));
					tr.append($('<td>' + user.email + '</td>'));
					tr.append($('<td>' + user.mobile + '</td>'));
					tr.append($('<td>' + user.nickname + '</td>'));
					tr.hide();
					tbody.append(tr);
					tr.fadeIn();
				}
			}else{
				var tr = $('<tr><td colspan="5" class="text-center"><b>Data Not Found</b></td></tr>');
				tbody.append(tr);
			}
		 }
		,error: function(response) {
			console.log(response);
			alert(response.responseText);
			return false;
		 }
	});	
}

/**
 * Getting user detail
 */
function getUser(id) {
	$.ajax({
		 type:'GET'
		,url:'${pageContext.request.contextPath}' + '/console/user/getUser'
		,data: {'id':id}
		,dataType:'json'
		,encode: true
		,success:function(user) {
			console.log(user);
			$('#id').val(user.id);
			$('#email').val(user.email);
			$('#mobile').val(user.mobile);
			$('#name').val(user.name);
			$('#nickname').val(user.nickname);
			$('#useYn').val(user.useYn);
		 }
		,error: function(response) {
			console.log(response);
			alert(response.responseText);
			return false;
		 }
	});	
}

</script>
<style type="text/css">
</style>

<div class="container-fluid">
	<div class="row">
		<div class="col">
			<h1>
				User Management
				<small>Management of User, Group, Role, Privilege </small>
			</h1>
		</div>
	</div>
	<div class="row">
		<div class="col"></div>
	</div>
	<div class="row">
		<div class="col">
			<div class="container-fluid border rounded">
				<div class="row" style="padding-top:20px; min-height:700px;">
					<div class="col">
						<div class="input-group">
							<span class="input-group-addon" id="basic-addon1">
								<i class="fa fa-search" aria-hidden="true"></i>
							</span>
							<select class="form-control col-3" style="font-weight:bold;">
								<option value="id">ID</option>
								<option value="name">Name</option>
								<option value="email">Email</option>
								<option value="mobile">Mobile</option>
								<option value="nickname">Nickname</option>
							</select>
							<input type="text" class="form-control" id="keyword" placeholder="Keyword">
							<button type="button" class="btn btn-primary">
								<i class="fa fa-search" aria-hidden="true"></i>
								Find User
							</button>
						</div>
						<table id="userListTable" class="table table-bordered table-hover">
							<colgroup>
								<col width="20%"/>
								<col width="20%"/>
								<col width="20%"/>
								<col width="20%"/>
								<col width="20%"/>
							</colgroup>
							<thead>
							    <tr>
							      <th scope="col">ID</th>
							      <th scope="col">Name</th>
							      <th scope="col">Email</th>
							      <th scope="col">Mobile</th>
							      <th scope="col">Nickname</th>
							    </tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						<ul class="pagination justify-content-center">
							<li class="page-item disabled">
								<a class="page-link" href="#" tabindex="-1"><</a>
							</li>
							<li class="page-item"><a class="page-link" href="#">1</a></li>
							<li class="page-item"><a class="page-link" href="#">2</a></li>
							<li class="page-item"><a class="page-link" href="#">3</a></li>
							<li class="page-item">
								<a class="page-link" href="#">></a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!--  end of left panel -->
		
		<!-- start of right panel -->
		<div class="col">
			<div class="container-fluid border rounded">
				<!-- start of user detail -->
				<div class="row" style="padding-top:10px; min-height:700px;">
					<div class="col">
						<h2>
							User
							<small>Details of user</small>
						</h2>
						<hr/>
						<div class="form-group row">
							<label for="id" class="col-2 col-form-label">ID</label>
							<div class="col-4">
								<input id="id" class="form-control" type="text" value="">
							</div>
						</div>
						<div class="form-group row">
							<label for="email" class="col-2 col-form-label">Email</label>
							<div class="col-4">
								<input id="email" class="form-control" type="text" value="">
							</div>
						</div>
						<div class="form-group row">
							<label for="mobile" class="col-2 col-form-label">Mobile</label>
							<div class="col-4">
								<input id="mobile" class="form-control" type="text" value="">
							</div>
						</div>
						<div class="form-group row">
							<label for="name" class="col-2 col-form-label">Name</label>
							<div class="col-6">
								<input id="name" class="form-control" type="text" value="">
							</div>
						</div>
						<div class="form-group row">
							<label for=""nickname"" class="col-2 col-form-label">Nickname</label>
							<div class="col-6">
								<input id="nickname" class="form-control" type="text" value="">
							</div>
						</div>
						<div class="form-group row">
							<label for="useYn" class="col-2 col-form-label">Use YN</label>
							<div class="col-2">
								<select id="useYn" class="form-control">
									<option value="Y">Y</option>
									<option value="N">N</option>
								</select>
							</div>
						</div>
						<div class="form-group row">
							<div class="col text-right">
								<button type="button" class="btn btn-primary">
									<i class="fa fa-floppy-o" aria-hidden="true"></i>
									Save
								</button>
								<button type="button" class="btn btn-danger">
									<i class="fa fa-trash" aria-hidden="true"></i>
									Remove
								</button>
							</div>
						</div>
						<h2>
							Groups
							<small>List of Assigned Group</small>
						</h2>
						<table class="table table-bordered">
							<thead>
							    <tr>
							      <th scope="col">Group ID</th>
							      <th scope="col">Name</th>
							      <th scope="col">Status</th>
							      <th scope="col">-</th>
							    </tr>
							</thead>
							<tbody>
								<tr>
									<td>SALES</td>
									<td>Sales Department</td>
									<td>USE</td>
									<td>
										<button type="button" class="btn btn-light"><i class="fa fa-trash" aria-hidden="true"></i></button>
									</td>
							    </tr>
								<tr>
									<td>SALES</td>
									<td>Sales Department</td>
									<td>USE</td>
									<td>
										<button type="button" class="btn btn-light"><i class="fa fa-trash" aria-hidden="true"></i></button>
									</td>
							    </tr>
								<tr>
									<td>SALES</td>
									<td>Sales Department</td>
									<td>USE</td>
									<td>
										<button type="button" class="btn btn-light"><i class="fa fa-trash" aria-hidden="true"></i></button>
									</td>
							    </tr>
							</tbody>
						</table>
						<div class="float-right">
							<button type="button" class="btn btn-light">
								<i class="fa fa-plus" aria-hidden="true"></i>
								Add
							</button>
						</div>
						<h2>
							Roles
							<small>List of Available Rules</small>
						</h2>
						<table class="table table-bordered">
						  <thead>
						    <tr>
						      <th scope="col">First Name</th>
						      <th scope="col">Last Name</th>
						      <th scope="col">Username</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>Mark</td>
						      <td>Otto</td>
						      <td>@mdo</td>
						    </tr>
						    <tr>
						      <td>Mark</td>
						      <td>Otto</td>
						      <td>@TwBootstrap</td>
						    </tr>
						    <tr>
						      <td>Jacob</td>
						      <td>Thornton</td>
						      <td>@fat</td>
						    </tr>
						  </tbody>
						</table>
						<h2>
							Privileges
							<small>List of Available Privileges</small>
						</h2>
						<table class="table table-bordered">
						  <thead>
						    <tr>
						      <th scope="col">#</th>
						      <th scope="col">First Name</th>
						      <th scope="col">Last Name</th>
						      <th scope="col">Username</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <th scope="row">1</th>
						      <td>Mark</td>
						      <td>Otto</td>
						      <td>@mdo</td>
						    </tr>
						    <tr>
						      <th scope="row">2</th>
						      <td>Mark</td>
						      <td>Otto</td>
						      <td>@TwBootstrap</td>
						    </tr>
						    <tr>
						      <th scope="row">3</th>
						      <td>Jacob</td>
						      <td>Thornton</td>
						      <td>@fat</td>
						    </tr>
						    <tr>
						      <th scope="row">4</th>
						      <td colspan="2">Larry the Bird</td>
						      <td>@twitter</td>
						    </tr>
						  </tbody>
						</table>
					</div>				
				</div>
				<!-- end of privilege -->
			</div>
				
		</div>
	</div>
	
</div>