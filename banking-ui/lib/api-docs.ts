export const apiDocs: Record<string, { desc: string; steps: string[]; notes?: string }> = {
  register: {
    desc: "Đăng ký tài khoản người dùng mới. Password được mã hóa BCrypt trước khi lưu vào database.",
    steps: [
      "Client gửi request POST đến API Gateway (không cần token).",
      "API Gateway chuyển tiếp đến User Service.",
      "Kiểm tra username đã tồn tại chưa → nếu trùng thì báo lỗi.",
      "Mã hóa password bằng BCrypt rồi lưu user vào database.",
      "Trả về thông tin user vừa tạo.",
    ],
    notes: "Username phải unique. Password được hash bằng BCrypt, không lưu plaintext.",
  },

  login: {
    desc: "Đăng nhập và nhận JWT token (hết hạn sau 10 phút). Token chứa userId dùng để xác thực ở các service khác.",
    steps: [
      "Client gửi request POST đến API Gateway (không cần token).",
      "API Gateway chuyển tiếp đến User Service.",
      "Tìm user theo username trong database.",
      "So sánh password đã nhập với password đã hash bằng BCrypt.",
      "Nếu khớp → tạo JWT token (thuật toán HS512, chứa userId, hết hạn 10 phút).",
      "Nếu sai → trả về 401 Unauthorized.",
    ],
    notes: "Token hết hạn sau 10 phút. Cần login lại để lấy token mới.",
  },

  getUsers: {
    desc: "Lấy danh sách tất cả người dùng trong hệ thống.",
    steps: [
      "Client gửi request GET kèm JWT token.",
      "API Gateway xác thực token → nếu hợp lệ thì chuyển tiếp đến User Service.",
      "User Service truy vấn toàn bộ bảng users trong database.",
      "Trả về danh sách user.",
    ],
  },

  createAcc: {
    desc: "Tạo tài khoản ngân hàng mới. Gọi chéo sang User Service (kiểm tra user) và Sequence Generator (tạo số tài khoản).",
    steps: [
      "Client gửi request POST kèm JWT token và loại tài khoản.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Trích xuất userId từ JWT token.",
      "Gọi chéo sang User Service (Feign Client) để xác nhận user tồn tại.",
      "Gọi chéo sang Sequence Generator để tạo số tài khoản tự động (prefix '060014' + 7 chữ số).",
      "Tạo tài khoản với trạng thái PENDING, số dư = 0.",
      "Lưu vào database và trả về thông tin tài khoản.",
    ],
    notes: "Tài khoản mới luôn ở trạng thái PENDING với số dư = 0. Cần nạp >= 1000 rồi mới activate được.",
  },

  accByUser: {
    desc: "Lấy tất cả tài khoản ngân hàng của một user. Chỉ chủ sở hữu mới xem được.",
    steps: [
      "Client gửi request GET kèm JWT token và User ID.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Kiểm tra quyền sở hữu: userId trong JWT phải trùng với userId truyền vào.",
      "Nếu không trùng → trả về 403 Forbidden.",
      "Truy vấn tất cả tài khoản theo userId và trả về danh sách.",
    ],
  },

  accByNum: {
    desc: "Lấy thông tin tài khoản theo số tài khoản. Chỉ chủ sở hữu mới xem được.",
    steps: [
      "Client gửi request GET kèm JWT token và số tài khoản.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Tìm tài khoản theo số tài khoản trong database.",
      "Kiểm tra quyền sở hữu: userId trong JWT phải trùng với userId của tài khoản.",
      "Nếu hợp lệ → trả về thông tin tài khoản.",
    ],
  },

  balance: {
    desc: "Xem số dư tài khoản. Chỉ chủ sở hữu mới xem được.",
    steps: [
      "Client gửi request GET kèm JWT token và số tài khoản.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Kiểm tra quyền sở hữu.",
      "Trả về số dư hiện tại của tài khoản.",
    ],
  },

  updateStatus: {
    desc: "Cập nhật trạng thái tài khoản (PENDING → ACTIVE). Yêu cầu số dư >= 1000.",
    steps: [
      "Client gửi request PATCH kèm JWT token, số tài khoản và trạng thái mới.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Kiểm tra quyền sở hữu.",
      "Kiểm tra tài khoản đã ACTIVE chưa → nếu rồi thì báo lỗi.",
      "Kiểm tra số dư >= 1000 → nếu không đủ thì báo lỗi.",
      "Cập nhật trạng thái và lưu vào database.",
    ],
    notes: "Chỉ chuyển từ PENDING → ACTIVE. Tài khoản đã ACTIVE sẽ báo lỗi.",
  },

  closeAcc: {
    desc: "Đóng tài khoản. Yêu cầu số dư phải bằng 0.",
    steps: [
      "Client gửi request PUT kèm JWT token và số tài khoản.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Kiểm tra quyền sở hữu.",
      "Kiểm tra số dư == 0 → nếu còn tiền thì báo lỗi.",
      "Chuyển trạng thái sang CLOSED và lưu vào database.",
    ],
    notes: "Rút hết tiền trước khi đóng tài khoản.",
  },

  accTx: {
    desc: "Lấy lịch sử giao dịch của tài khoản. Account Service gọi chéo sang Transaction Service.",
    steps: [
      "Client gửi request GET kèm JWT token và số tài khoản.",
      "API Gateway xác thực token → chuyển tiếp đến Account Service.",
      "Kiểm tra quyền sở hữu.",
      "Account Service gọi chéo sang Transaction Service (Feign Client) để lấy danh sách giao dịch.",
      "Trả về danh sách giao dịch của tài khoản.",
    ],
  },

  deposit: {
    desc: "Nạp tiền vào tài khoản. Cộng số tiền vào balance, tạo transaction record. Không yêu cầu tài khoản ACTIVE.",
    steps: [
      "Client gửi request POST kèm JWT token, số tài khoản, số tiền và mô tả.",
      "API Gateway xác thực token → chuyển tiếp đến Transaction Service.",
      "Kiểm tra quyền sở hữu (userId trong JWT phải trùng với userId của tài khoản).",
      "Gọi chéo sang Account Service (Feign Client) để đọc thông tin tài khoản.",
      "Cộng số tiền vào balance của tài khoản.",
      "Gọi chéo sang Account Service để cập nhật balance mới.",
      "Tạo transaction record với trạng thái COMPLETED và lưu vào database.",
    ],
    notes: "Deposit không yêu cầu tài khoản ACTIVE. Nạp >= 1000 để có thể activate tài khoản.",
  },

  withdrawal: {
    desc: "Rút tiền từ tài khoản. Yêu cầu tài khoản ACTIVE và đủ số dư.",
    steps: [
      "Client gửi request POST kèm JWT token, số tài khoản, số tiền và mô tả.",
      "API Gateway xác thực token → chuyển tiếp đến Transaction Service.",
      "Kiểm tra quyền sở hữu.",
      "Gọi chéo sang Account Service để đọc thông tin tài khoản.",
      "Kiểm tra tài khoản có ACTIVE không → nếu không thì báo lỗi.",
      "Kiểm tra số dư >= số tiền rút → nếu không đủ thì báo lỗi.",
      "Trừ số tiền khỏi balance, cập nhật Account Service.",
      "Tạo transaction record với trạng thái COMPLETED.",
    ],
    notes: "Tài khoản PENDING hoặc BLOCKED không thể rút tiền.",
  },

  txByAcc: {
    desc: "Lấy tất cả giao dịch (deposit, withdrawal, transfer) của một tài khoản.",
    steps: [
      "Client gửi request GET kèm JWT token và số tài khoản.",
      "API Gateway xác thực token → chuyển tiếp đến Transaction Service.",
      "Kiểm tra quyền sở hữu.",
      "Truy vấn tất cả giao dịch theo accountId trong database.",
      "Trả về danh sách giao dịch.",
    ],
  },

  txByRef: {
    desc: "Lấy giao dịch theo mã tham chiếu (reference ID). Dùng để tra cứu giao dịch chuyển tiền.",
    steps: [
      "Client gửi request GET kèm JWT token và reference ID.",
      "API Gateway xác thực token → chuyển tiếp đến Transaction Service.",
      "Truy vấn giao dịch theo referenceId trong database.",
      "Trả về danh sách giao dịch (mỗi fund transfer tạo 2 records cùng referenceId: 1 trừ tiền, 1 cộng tiền).",
    ],
    notes: "Mỗi fund transfer tạo 2 transaction records cùng referenceId: 1 trừ tiền bên gửi, 1 cộng tiền bên nhận.",
  },

  transfer: {
    desc: "Chuyển tiền giữa 2 tài khoản. Gọi chéo Account Service (cập nhật balance) và Transaction Service (tạo records).",
    steps: [
      "Client gửi request POST kèm JWT token, tài khoản gửi, tài khoản nhận và số tiền.",
      "API Gateway xác thực token → chuyển tiếp đến Fund Transfer Service.",
      "Gọi chéo sang Account Service để đọc thông tin tài khoản gửi.",
      "Kiểm tra tài khoản gửi có ACTIVE không → nếu không thì báo lỗi.",
      "Kiểm tra số dư tài khoản gửi >= số tiền chuyển → nếu không đủ thì báo lỗi.",
      "Gọi chéo sang Account Service để đọc thông tin tài khoản nhận.",
      "Trừ tiền tài khoản gửi, cộng tiền tài khoản nhận → cập nhật cả 2 qua Account Service.",
      "Gọi chéo sang Transaction Service để tạo 2 transaction records (cùng referenceId).",
      "Lưu fund transfer record với trạng thái SUCCESS.",
      "Trả về referenceId để tra cứu sau.",
    ],
    notes: "Cả 2 tài khoản phải tồn tại. Tài khoản gửi phải ACTIVE và đủ số dư.",
  },

  transferDetail: {
    desc: "Xem chi tiết giao dịch chuyển tiền theo mã tham chiếu (reference ID).",
    steps: [
      "Client gửi request GET kèm JWT token và reference ID.",
      "API Gateway xác thực token → chuyển tiếp đến Fund Transfer Service.",
      "Tìm fund transfer record theo referenceId trong database.",
      "Trả về thông tin chi tiết: tài khoản gửi, tài khoản nhận, số tiền, trạng thái.",
    ],
  },
};
