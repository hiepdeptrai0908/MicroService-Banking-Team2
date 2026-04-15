import {
  Users, Landmark, ArrowLeftRight, CreditCard, LogIn, UserPlus,
  PlusCircle, Search, DollarSign, ShieldCheck, XCircle, List,
  ArrowDownToLine, ArrowUpFromLine, Send, FileText, Eye,
} from "lucide-react";
import type { ElementType } from "react";

export type NavItem = { id: string; label: string; icon: ElementType; method: string; desc: string };
export type NavGroup = { id: string; label: string; icon: ElementType; port: number; items: NavItem[] };

export const nav: NavGroup[] = [
  {
    id: "user", label: "User Service", icon: Users, port: 8081,
    items: [
      { id: "register", label: "Register", icon: UserPlus, method: "POST", desc: "Đăng ký tài khoản người dùng" },
      { id: "login", label: "Login", icon: LogIn, method: "POST", desc: "Đăng nhập, lấy JWT token" },
      { id: "getUsers", label: "Get All Users", icon: List, method: "GET", desc: "Lấy danh sách tất cả người dùng" },
    ],
  },
  {
    id: "account", label: "Account Service", icon: Landmark, port: 8082,
    items: [
      { id: "createAcc", label: "Create Account", icon: PlusCircle, method: "POST", desc: "Tạo tài khoản ngân hàng mới" },
      { id: "accByUser", label: "Get By User ID", icon: Search, method: "GET", desc: "Lấy tài khoản theo User ID" },
      { id: "accByNum", label: "Get By Account Number", icon: Search, method: "GET", desc: "Lấy tài khoản theo số tài khoản" },
      { id: "balance", label: "Get Balance", icon: DollarSign, method: "GET", desc: "Xem số dư tài khoản" },
      { id: "updateStatus", label: "Update Status", icon: ShieldCheck, method: "PATCH", desc: "Cập nhật trạng thái (ACTIVE, BLOCKED...)" },
      { id: "closeAcc", label: "Close Account", icon: XCircle, method: "PUT", desc: "Đóng tài khoản (yêu cầu số dư = 0)" },
      { id: "accTx", label: "Get Transactions", icon: List, method: "GET", desc: "Lấy lịch sử giao dịch của tài khoản" },
    ],
  },
  {
    id: "transaction", label: "Transaction Service", icon: CreditCard, port: 8084,
    items: [
      { id: "deposit", label: "Deposit", icon: ArrowDownToLine, method: "POST", desc: "Nạp tiền vào tài khoản" },
      { id: "withdrawal", label: "Withdrawal", icon: ArrowUpFromLine, method: "POST", desc: "Rút tiền từ tài khoản" },
      { id: "txByAcc", label: "Get By Account", icon: List, method: "GET", desc: "Lấy giao dịch theo số tài khoản" },
      { id: "txByRef", label: "Get By Reference", icon: FileText, method: "GET", desc: "Lấy giao dịch theo mã tham chiếu" },
    ],
  },
  {
    id: "transfer", label: "Fund Transfer", icon: ArrowLeftRight, port: 8083,
    items: [
      { id: "transfer", label: "Transfer", icon: Send, method: "POST", desc: "Chuyển tiền giữa 2 tài khoản" },
      { id: "transferDetail", label: "Get Detail", icon: Eye, method: "GET", desc: "Xem chi tiết giao dịch chuyển tiền" },
    ],
  },
];

export const methodColor: Record<string, string> = {
  GET: "text-green-400", POST: "text-yellow-400", PUT: "text-blue-400", PATCH: "text-orange-400", DELETE: "text-red-400",
};
