package DropWizard;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import Database.CacheUserHistory;
import Database.DatabaseUtils;
import Recommend.RecommendationSpark;
import Recommend.SANPHAM;
import scala.collection.mutable.WrappedArray;

@Path("/recommend")
@Produces(MediaType.APPLICATION_JSON)
public class RecommendController {
	private final Validator validator;

	public RecommendController(Validator validator) {
		this.validator = validator;
	}

	@GET
	public Response getRatingsPrediction() {
		//RecommendationSpark.recommendForAllUser(50);
		return Response.ok("response GET").build();
	}

	@GET
	@Path("/{id}")
	public Response getRatingsPredictionById(@Context HttpServletRequest request, @PathParam("id") Integer id) throws SQLException {

		return Response.ok(DatabaseUtils.iipredictForUserNumItem(id,  10, request.getRemoteAddr())).build();
		
	}

	@GET
	@Path("/{id}/{numItem}")
	public Response getRatingsPredictionById(@Context HttpServletRequest request, @PathParam("id") Integer id, @PathParam("numItem") Integer numItem)
			throws SQLException {
		ArrayList<SANPHAM> listSP;
		if (id == 0)
			listSP = DatabaseUtils.getListSanPhamTuongTu(request.getRemoteAddr(), 4);
		else 
			listSP = DatabaseUtils.getListSanPhamTuongTu(String.valueOf(id), 2);
		
		listSP.addAll(DatabaseUtils.iipredictForUserNumItem(id,  numItem, request.getRemoteAddr()));
		return Response.ok(listSP).build();
	}
	
	@GET
	@Path("viewed/start/{userId}/{itemId}/{itemType}")
	public Response startView(@Context HttpServletRequest request, @PathParam("userId") String userId, @PathParam("itemId") Integer itemId,
			@PathParam("itemType") String itemType) throws SQLException {
		if (userId.equals("0"))
			userId = request.getRemoteAddr();
		CacheUserHistory.userStartViewItem(userId,  itemId, itemType);
		return Response.ok("STARTVIEW "+ userId + " item: " + itemId.toString()).build();
		
	}
	
	@GET
	@Path("viewed/end/{userId}/{itemId}")
	public Response endViewed(@Context HttpServletRequest request, @PathParam("userId") String userId, @PathParam("itemId") Integer itemId) throws SQLException {
		if (userId.equals("0"))
			userId = request.getRemoteAddr();
		CacheUserHistory.userEndViewItem(userId, itemId);
		return Response.ok("ENDVIEW "+ userId + " item: " + itemId.toString()).build();
		
	}
	
	@GET
	@Path("viewed/logout/{userId}/{itemId}")
	public Response logOut(@Context HttpServletRequest request, @PathParam("userId") String userId, @PathParam("itemId") Integer itemId) throws SQLException {
		if (userId.equals("0"))
			userId = request.getRemoteAddr();
		CacheUserHistory.userEndViewItem(userId,  itemId);
		return Response.ok("LOGOUT "+ userId + " item: " + itemId.toString()).build();
		
	}

}
