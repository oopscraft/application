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
					tr.append($('<td>' + user.id + '</td>'));
					tr.append($('<td>' + user.email + '</td>'));
					tr.append($('<td>' + user.mobileNumber + '</td>'));
					tr.append($('<td>' + user.name + '</td>'));
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

</script>
<style type="text/css">
</style>
<h1>
	User Management
	<small>Management of User, Group, Role, Privilege </small>
</h1>
<div class="row">

	<!-- start of left panel -->
	<div class="col">
		<!--  start of search form -->
		<div class="row">
			<div class="col text-left">
				<form class="form-inline">
					<select class="form-control custom-select">
						<option selected>___Column___</option>
						<option value="1">One</option>
						<option value="2">Two</option>
						<option value="3">Three</option>
					</select>
					<input type="text" class="form-control" id="keyword" placeholder="Keyword">
					<button type="button" class="btn">
						<i class="fa fa-floppy-o" aria-hidden="true"></i>
						Search
					</button>
				</form>
			</div>
			<div class="col">
				<ul class="pagination justify-content-end">
					<li class="page-item disabled">
						<a class="page-link" href="#" tabindex="-1"><i class="fa fa-caret-left" aria-hidden="true"></i></a>
					</li>
					<li class="page-item"><a class="page-link" href="#">3</a></li>
					<li class="page-item">
						<a class="page-link" href="#"><i class="fa fa-caret-right" aria-hidden="true"></i></a>
					</li>
				</ul>
			</div>
		</div>
		<!-- end of search form -->
		
		<!-- start of user list table -->
		<table id="userListTable" class="table table-bordered">
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
			      <th scope="col">Email</th>
			      <th scope="col">Mobile</th>
			      <th scope="col">Name</th>
			      <th scope="col">Nickname</th>
			    </tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<!-- end of user list table -->

	</div>
	<!--  end of left panel -->
	
	<!-- start of right panel -->
	<div class="col">
	
		<!-- start of user detail -->
		<div class="row">
			<div class="col">
				<h2>
					<i class="fa fa-user" aria-hidden="true"></i>
					User
					<small>Details of user</small>
				</h2>
				<div class="border rounded" style="padding:10px;">
					<form>
						  <div class="form-row">
						    <div class="form-group col-md-6">
						      <label for="inputEmail4">Email</label>
						      <input type="email" class="form-control" id="inputEmail4" placeholder="Email">
						    </div>
						    <div class="form-group col-md-6">
						      <label for="inputPassword4">Password</label>
						      <input type="password" class="form-control" id="inputPassword4" placeholder="Password">
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="inputAddress">Address</label>
						    <input type="text" class="form-control" id="inputAddress" placeholder="1234 Main St">
						  </div>
						  <div class="form-group">
						    <label for="inputAddress2">Address 2</label>
						    <input type="text" class="form-control" id="inputAddress2" placeholder="Apartment, studio, or floor">
						  </div>
						  <div class="form-row">
						    <div class="form-group col-md-6">
						      <label for="inputCity">City</label>
						      <input type="text" class="form-control" id="inputCity">
						    </div>
						    <div class="form-group col-md-4">
						      <label for="inputState">State</label>
						      <select id="inputState" class="form-control">
						        <option selected>Choose...</option>
						        <option>...</option>
						      </select>
						    </div>
						    <div class="form-group col-md-2">
						      <label for="inputZip">Zip</label>
						      <input type="text" class="form-control" id="inputZip">
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="form-check">
						      <label class="form-check-label">
						        <input class="form-check-input" type="checkbox"> Check me out
						      </label>
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
					</form>
				</div>
			</div>
		</div>
		<!-- end of user detail -->
		
		<!-- start of group -->
		<div class="row">
			<div class="col">		
				<h2>
					<i class="fa fa-group" aria-hidden="true"></i>
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
			</div>				
		</div>
		<!-- end of group -->

		<!-- start of role -->
		<div class="row">
			<div class="col">
				<h2>
					<i class="fa fa-id-badge" aria-hidden="true"></i>
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
			</div>				
		</div>
		<!-- end of role -->
		
		<!-- start of privilege -->
		<div class="row">
			<div class="col">
				<h2>
					<i class="fa fa-barcode" aria-hidden="true"></i>
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