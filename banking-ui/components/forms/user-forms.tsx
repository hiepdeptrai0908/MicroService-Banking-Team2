"use client";
import { UserPlus, LogIn, List } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { PasswordInput } from "@/components/password-input";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { ResultPanel } from "@/components/result-panel";
import { api, type ApiRes } from "@/lib/api";

type Props = {
  res: Record<string, ApiRes>;
  set: (k: string, v: ApiRes) => void;
  token: string;
  setToken: (t: string) => void;
  h: () => HeadersInit;
};

export function RegisterForm({
  state,
  props,
}: {
  state: {
    regName: string;
    setRegName: (v: string) => void;
    regUser: string;
    setRegUser: (v: string) => void;
    regPass: string;
    setRegPass: (v: string) => void;
  };
  props: Props;
}) {
  const { regName, setRegName, regUser, setRegUser, regPass, setRegPass } =
    state;
  return (
    <>
      <Input
        placeholder="Full Name"
        value={regName}
        onChange={(e) => setRegName(e.target.value)}
      />
      <Input
        placeholder="Username"
        value={regUser}
        onChange={(e) => setRegUser(e.target.value)}
      />
      <PasswordInput
        value={regPass}
        onChange={(e) => setRegPass(e.target.value)}
      />
      <ConfirmDialog
        method="POST"
        url="/api/users/register"
        body={{ fullName: regName, username: regUser, password: regPass }}
        trigger={
          <Button className="w-full">
            <UserPlus className="h-4 w-4 mr-2" />
            Register
          </Button>
        }
        onConfirm={async () =>
          props.set(
            "register",
            await api("/api/users/register", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                fullName: regName,
                username: regUser,
                password: regPass,
              }),
            }),
          )
        }
      />
      <ResultPanel res={props.res.register} />
    </>
  );
}

export function LoginForm({
  state,
  props,
}: {
  state: {
    loginUser: string;
    setLoginUser: (v: string) => void;
    loginPass: string;
    setLoginPass: (v: string) => void;
  };
  props: Props;
}) {
  const { loginUser, setLoginUser, loginPass, setLoginPass } = state;
  return (
    <>
      <Input
        placeholder="Username"
        value={loginUser}
        onChange={(e) => setLoginUser(e.target.value)}
      />
      <PasswordInput
        value={loginPass}
        onChange={(e) => setLoginPass(e.target.value)}
      />
      <ConfirmDialog
        method="POST"
        url="/api/users/login"
        body={{ username: loginUser, password: loginPass }}
        trigger={
          <Button className="w-full" variant="secondary">
            <LogIn className="h-4 w-4 mr-2" />
            Login
          </Button>
        }
        onConfirm={async () => {
          const r = await api("/api/users/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username: loginUser, password: loginPass }),
          });
          props.set("login", r);
          if (
            r &&
            r.status === 200 &&
            typeof r.data === "object" &&
            r.data !== null &&
            "token" in r.data
          )
            props.setToken((r.data as { token: string }).token);
        }}
      />
      <ResultPanel res={props.res.login} />
    </>
  );
}

export function GetUsersForm({ props }: { props: Props }) {
  return (
    <>
      <ConfirmDialog
        method="GET"
        url="/api/users"
        trigger={
          <Button className="w-full" variant="outline">
            <List className="h-4 w-4 mr-2" />
            Fetch All Users
          </Button>
        }
        onConfirm={async () =>
          props.set("users", await api("/api/users", { headers: props.h() }))
        }
      />
      <ResultPanel res={props.res.users} />
    </>
  );
}
