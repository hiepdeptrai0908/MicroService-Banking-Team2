"use client";
import {
  PlusCircle,
  Search,
  DollarSign,
  ShieldCheck,
  XCircle,
  List,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { ResultPanel } from "@/components/result-panel";
import { api, type ApiRes } from "@/lib/api";

type Props = {
  res: Record<string, ApiRes>;
  set: (k: string, v: ApiRes) => void;
  h: () => HeadersInit;
};

export function CreateAccountForm({
  accType,
  setAccType,
  setAccNum,
  setFromAcc,
  props,
}: {
  accType: string;
  setAccType: (v: string) => void;
  setAccNum: (v: string) => void;
  setFromAcc: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Select value={accType} onValueChange={(v) => v && setAccType(v)}>
        <SelectTrigger>
          <SelectValue />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="SAVINGS_ACCOUNT">Savings Account</SelectItem>
          <SelectItem value="FIXED_DEPOSIT">Fixed Deposit</SelectItem>
          <SelectItem value="LOAN_ACCOUNT">Loan Account</SelectItem>
        </SelectContent>
      </Select>
      <ConfirmDialog
        method="POST"
        url="/api/accounts"
        body={{ accountType: accType }}
        trigger={
          <Button className="w-full">
            <PlusCircle className="h-4 w-4 mr-2" />
            Create Account
          </Button>
        }
        onConfirm={async () => {
          const r = await api("/api/accounts", {
            method: "POST",
            headers: props.h(),
            body: JSON.stringify({ accountType: accType }),
          });
          props.set("createAcc", r);
          if (
            r &&
            r.status < 400 &&
            typeof r.data === "object" &&
            r.data !== null &&
            "accountNumber" in r.data
          ) {
            const num = (r.data as { accountNumber: string }).accountNumber;
            setAccNum(num);
            setFromAcc(num);
          }
        }}
      />
      <ResultPanel res={props.res.createAcc} />
    </>
  );
}

export function AccByUserForm({
  userId,
  setUserId,
  props,
}: {
  userId: string;
  setUserId: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="User ID"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />
      <ConfirmDialog
        method="GET"
        url={`/api/accounts/${userId}`}
        trigger={
          <Button className="w-full" variant="outline">
            <Search className="h-4 w-4 mr-2" />
            Search
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "accByUser",
            await api(`/api/accounts/${userId}`, { headers: props.h() }),
          )
        }
      />
      <ResultPanel res={props.res.accByUser} />
    </>
  );
}

export function AccByNumForm({
  accNum,
  setAccNum,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <ConfirmDialog
        method="GET"
        url={`/api/accounts?accountNumber=${accNum}`}
        trigger={
          <Button className="w-full" variant="outline">
            <Search className="h-4 w-4 mr-2" />
            Search
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "accByNum",
            await api(`/api/accounts?accountNumber=${accNum}`, {
              headers: props.h(),
            }),
          )
        }
      />
      <ResultPanel res={props.res.accByNum} />
    </>
  );
}

export function BalanceForm({
  accNum,
  setAccNum,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <ConfirmDialog
        method="GET"
        url={`/api/accounts/balance?accountNumber=${accNum}`}
        trigger={
          <Button className="w-full" variant="outline">
            <DollarSign className="h-4 w-4 mr-2" />
            Get Balance
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "balance",
            await api(`/api/accounts/balance?accountNumber=${accNum}`, {
              headers: props.h(),
            }),
          )
        }
      />
      <ResultPanel res={props.res.balance} />
    </>
  );
}

export function UpdateStatusForm({
  accNum,
  setAccNum,
  accStatus,
  setAccStatus,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  accStatus: string;
  setAccStatus: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <Select value={accStatus} onValueChange={(v) => v && setAccStatus(v)}>
        <SelectTrigger>
          <SelectValue />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="PENDING">Pending</SelectItem>
          <SelectItem value="ACTIVE">Active</SelectItem>
          <SelectItem value="BLOCKED">Blocked</SelectItem>
          <SelectItem value="CLOSED">Closed</SelectItem>
        </SelectContent>
      </Select>
      <ConfirmDialog
        method="PATCH"
        url={`/api/accounts?accountNumber=${accNum}`}
        body={{ accountStatus: accStatus }}
        trigger={
          <Button className="w-full" variant="secondary">
            <ShieldCheck className="h-4 w-4 mr-2" />
            Update Status
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "updateStatus",
            await api(`/api/accounts?accountNumber=${accNum}`, {
              method: "PATCH",
              headers: props.h(),
              body: JSON.stringify({ accountStatus: accStatus }),
            }),
          )
        }
      />
      <ResultPanel res={props.res.updateStatus} />
    </>
  );
}

export function CloseAccountForm({
  accNum,
  setAccNum,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <p className="text-xs text-muted-foreground">
        Yêu cầu số dư tài khoản bằng 0 để đóng.
      </p>
      <ConfirmDialog
        method="PUT"
        url={`/api/accounts/closure?accountNumber=${accNum}`}
        trigger={
          <Button className="w-full" variant="destructive">
            <XCircle className="h-4 w-4 mr-2" />
            Close Account
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "closeAcc",
            await api(`/api/accounts/closure?accountNumber=${accNum}`, {
              method: "PUT",
              headers: props.h(),
            }),
          )
        }
      />
      <ResultPanel res={props.res.closeAcc} />
    </>
  );
}

export function AccTxForm({
  accNum,
  setAccNum,
  props,
}: {
  accNum: string;
  setAccNum: (v: string) => void;
  props: Props;
}) {
  return (
    <>
      <Input
        placeholder="Account Number"
        value={accNum}
        onChange={(e) => setAccNum(e.target.value)}
      />
      <ConfirmDialog
        method="GET"
        url={`/api/accounts/${accNum}/transactions`}
        trigger={
          <Button className="w-full" variant="outline">
            <List className="h-4 w-4 mr-2" />
            Get Transactions
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "accTx",
            await api(`/api/accounts/${accNum}/transactions`, {
              headers: props.h(),
            }),
          )
        }
      />
      <ResultPanel res={props.res.accTx} />
    </>
  );
}
