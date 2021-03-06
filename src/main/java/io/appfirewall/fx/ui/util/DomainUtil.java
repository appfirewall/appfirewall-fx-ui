package io.appfirewall.fx.ui.util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DomainUtil {
    private final static Set<String> twoPartExtensions = new ConcurrentHashMap<>().newKeySet();

    static {
        // A list that should contain most Top-level domains (TLDs) that have a two part extension
        // It's a subset of the Public Suffix List
        twoPartExtensions.add("ab.ca");
        twoPartExtensions.add("ac.ac");
        twoPartExtensions.add("ac.ae");
        twoPartExtensions.add("ac.at");
        twoPartExtensions.add("ac.be");
        twoPartExtensions.add("ac.cn");
        twoPartExtensions.add("ac.il");
        twoPartExtensions.add("ac.in");
        twoPartExtensions.add("ac.jp");
        twoPartExtensions.add("ac.kr");
        twoPartExtensions.add("ac.th");
        twoPartExtensions.add("ac.uk");
        twoPartExtensions.add("ac.sg");
        twoPartExtensions.add("ad.jp");
        twoPartExtensions.add("adm.br");
        twoPartExtensions.add("adv.br");
        twoPartExtensions.add("ah.cn");
        twoPartExtensions.add("am.br");
        twoPartExtensions.add("arq.br");
        twoPartExtensions.add("art.br");
        twoPartExtensions.add("arts.ro");
        twoPartExtensions.add("asn.au");
        twoPartExtensions.add("asso.fr");
        twoPartExtensions.add("asso.mc");
        twoPartExtensions.add("bc.ca");
        twoPartExtensions.add("bio.br");
        twoPartExtensions.add("biz.pl");
        twoPartExtensions.add("biz.tr");
        twoPartExtensions.add("bj.cn");
        twoPartExtensions.add("bel.tr");
        twoPartExtensions.add("br.com");
        twoPartExtensions.add("cn.com");
        twoPartExtensions.add("cng.br");
        twoPartExtensions.add("cnt.br");
        twoPartExtensions.add("co.ac");
        twoPartExtensions.add("co.at");
        twoPartExtensions.add("co.de");
        twoPartExtensions.add("co.gl");
        twoPartExtensions.add("co.hk");
        twoPartExtensions.add("co.id");
        twoPartExtensions.add("co.il");
        twoPartExtensions.add("co.in");
        twoPartExtensions.add("co.jp");
        twoPartExtensions.add("co.kr");
        twoPartExtensions.add("co.mg");
        twoPartExtensions.add("co.ms");
        twoPartExtensions.add("co.nz");
        twoPartExtensions.add("co.th");
        twoPartExtensions.add("cp.tz");
        twoPartExtensions.add("co.uk");
        twoPartExtensions.add("co.ve");
        twoPartExtensions.add("co.vi");
        twoPartExtensions.add("co.za");
        twoPartExtensions.add("com.ag");
        twoPartExtensions.add("com.ai");
        twoPartExtensions.add("com.ar");
        twoPartExtensions.add("com.au");
        twoPartExtensions.add("com.br");
        twoPartExtensions.add("com.co");
        twoPartExtensions.add("com.cn");
        twoPartExtensions.add("com.cy");
        twoPartExtensions.add("com.de");
        twoPartExtensions.add("com.do");
        twoPartExtensions.add("com.ec");
        twoPartExtensions.add("com.es");
        twoPartExtensions.add("com.fj");
        twoPartExtensions.add("com.fr");
        twoPartExtensions.add("com.gl");
        twoPartExtensions.add("com.gt");
        twoPartExtensions.add("com.hk");
        twoPartExtensions.add("com.hr");
        twoPartExtensions.add("com.hu");
        twoPartExtensions.add("com.kg");
        twoPartExtensions.add("com.ki");
        twoPartExtensions.add("com.lc");
        twoPartExtensions.add("com.mg");
        twoPartExtensions.add("com.mm");
        twoPartExtensions.add("com.ms");
        twoPartExtensions.add("com.mt");
        twoPartExtensions.add("com.mu");
        twoPartExtensions.add("com.mx");
        twoPartExtensions.add("com.my");
        twoPartExtensions.add("com.na");
        twoPartExtensions.add("com.nf");
        twoPartExtensions.add("com.ng");
        twoPartExtensions.add("com.ni");
        twoPartExtensions.add("com.pa");
        twoPartExtensions.add("com.ph");
        twoPartExtensions.add("com.pl");
        twoPartExtensions.add("com.pt");
        twoPartExtensions.add("com.qa");
        twoPartExtensions.add("com.ro");
        twoPartExtensions.add("com.ru");
        twoPartExtensions.add("com.sb");
        twoPartExtensions.add("com.sc");
        twoPartExtensions.add("com.sg");
        twoPartExtensions.add("com.sv");
        twoPartExtensions.add("com.tr");
        twoPartExtensions.add("com.tw");
        twoPartExtensions.add("com.ua");
        twoPartExtensions.add("com.uy");
        twoPartExtensions.add("com.ve");
        twoPartExtensions.add("com.vn");
        twoPartExtensions.add("cq.cn");
        twoPartExtensions.add("de.com");
        twoPartExtensions.add("de.org");
        twoPartExtensions.add("ecn.br");
        twoPartExtensions.add("ed.jp");
        twoPartExtensions.add("edu.au");
        twoPartExtensions.add("edu.cn");
        twoPartExtensions.add("edu.hk");
        twoPartExtensions.add("edu.mm");
        twoPartExtensions.add("edu.my");
        twoPartExtensions.add("edu.pl");
        twoPartExtensions.add("edu.pt");
        twoPartExtensions.add("edu.qa");
        twoPartExtensions.add("edu.sg");
        twoPartExtensions.add("edu.tr");
        twoPartExtensions.add("edu.tw");
        twoPartExtensions.add("eng.br");
        twoPartExtensions.add("ernet.in");
        twoPartExtensions.add("esp.br");
        twoPartExtensions.add("etc.br");
        twoPartExtensions.add("eti.br");
        twoPartExtensions.add("eu.com");
        twoPartExtensions.add("eu.int");
        twoPartExtensions.add("eu.lv");
        twoPartExtensions.add("firm.in");
        twoPartExtensions.add("firm.ro");
        twoPartExtensions.add("fm.br");
        twoPartExtensions.add("fot.br");
        twoPartExtensions.add("fst.br");
        twoPartExtensions.add("g12.br");
        twoPartExtensions.add("gb.com");
        twoPartExtensions.add("gb.net");
        twoPartExtensions.add("gd.cn");
        twoPartExtensions.add("gen.in");
        twoPartExtensions.add("go.jp");
        twoPartExtensions.add("go.kr");
        twoPartExtensions.add("go.th");
        twoPartExtensions.add("gov.au");
        twoPartExtensions.add("gov.az");
        twoPartExtensions.add("gov.br");
        twoPartExtensions.add("gov.cn");
        twoPartExtensions.add("gov.il");
        twoPartExtensions.add("gov.in");
        twoPartExtensions.add("gov.mm");
        twoPartExtensions.add("gov.my");
        twoPartExtensions.add("gov.qa");
        twoPartExtensions.add("gov.sg");
        twoPartExtensions.add("gov.tr");
        twoPartExtensions.add("gov.tw");
        twoPartExtensions.add("gov.uk");
        twoPartExtensions.add("govt.nz");
        twoPartExtensions.add("gr.jp");
        twoPartExtensions.add("gs.cn");
        twoPartExtensions.add("gv.ac");
        twoPartExtensions.add("gv.at");
        twoPartExtensions.add("gx.cn");
        twoPartExtensions.add("gz.cn");
        twoPartExtensions.add("he.cn");
        twoPartExtensions.add("hi.cn");
        twoPartExtensions.add("hk.cn");
        twoPartExtensions.add("hl.cn");
        twoPartExtensions.add("hu.com");
        twoPartExtensions.add("id.au");
        twoPartExtensions.add("idv.tw");
        twoPartExtensions.add("in.ua");
        twoPartExtensions.add("in.th");
        twoPartExtensions.add("ind.br");
        twoPartExtensions.add("ind.in");
        twoPartExtensions.add("inf.br");
        twoPartExtensions.add("info.pl");
        twoPartExtensions.add("info.ro");
        twoPartExtensions.add("info.tr");
        twoPartExtensions.add("info.ve");
        twoPartExtensions.add("iwi.nz");
        twoPartExtensions.add("jl.cn");
        twoPartExtensions.add("jor.br");
        twoPartExtensions.add("js.cn");
        twoPartExtensions.add("jus.br");
        twoPartExtensions.add("k12.il");
        twoPartExtensions.add("k12.tr");
        twoPartExtensions.add("kr.com");
        twoPartExtensions.add("lel.br");
        twoPartExtensions.add("lg.jp");
        twoPartExtensions.add("ln.cn");
        twoPartExtensions.add("ltd.uk");
        twoPartExtensions.add("maori.nz");
        twoPartExtensions.add("mb.ca");
        twoPartExtensions.add("me.uk");
        twoPartExtensions.add("med.br");
        twoPartExtensions.add("mi.th");
        twoPartExtensions.add("mil.br");
        twoPartExtensions.add("mil.uk");
        twoPartExtensions.add("mo.cn");
        twoPartExtensions.add("mod.uk");
        twoPartExtensions.add("muni.il");
        twoPartExtensions.add("nb.ca");
        twoPartExtensions.add("ne.jp");
        twoPartExtensions.add("ne.kr");
        twoPartExtensions.add("net.ag");
        twoPartExtensions.add("net.ai");
        twoPartExtensions.add("net.au");
        twoPartExtensions.add("net.br");
        twoPartExtensions.add("net.cn");
        twoPartExtensions.add("net.do");
        twoPartExtensions.add("net.gl");
        twoPartExtensions.add("net.hk");
        twoPartExtensions.add("net.il");
        twoPartExtensions.add("net.in");
        twoPartExtensions.add("net.kg");
        twoPartExtensions.add("net.ki");
        twoPartExtensions.add("net.lc");
        twoPartExtensions.add("net.mg");
        twoPartExtensions.add("net.mm");
        twoPartExtensions.add("net.mu");
        twoPartExtensions.add("net.ni");
        twoPartExtensions.add("net.nz");
        twoPartExtensions.add("net.pl");
        twoPartExtensions.add("net.ru");
        twoPartExtensions.add("net.sb");
        twoPartExtensions.add("net.sc");
        twoPartExtensions.add("net.sg");
        twoPartExtensions.add("net.th");
        twoPartExtensions.add("net.tr");
        twoPartExtensions.add("net.tw");
        twoPartExtensions.add("net.uk");
        twoPartExtensions.add("net.ve");
        twoPartExtensions.add("nf.ca");
        twoPartExtensions.add("nhs.uk");
        twoPartExtensions.add("nm.cn");
        twoPartExtensions.add("nm.kr");
        twoPartExtensions.add("no.com");
        twoPartExtensions.add("nom.br");
        twoPartExtensions.add("nom.ni");
        twoPartExtensions.add("nom.ro");
        twoPartExtensions.add("ns.ca");
        twoPartExtensions.add("nt.ca");
        twoPartExtensions.add("nt.ro");
        twoPartExtensions.add("ntr.br");
        twoPartExtensions.add("nx.cn");
        twoPartExtensions.add("odo.br");
        twoPartExtensions.add("off.ai");
        twoPartExtensions.add("on.ca");
        twoPartExtensions.add("or.ac");
        twoPartExtensions.add("or.at");
        twoPartExtensions.add("or.jp");
        twoPartExtensions.add("or.kr");
        twoPartExtensions.add("or.th");
        twoPartExtensions.add("org.ag");
        twoPartExtensions.add("org.ai");
        twoPartExtensions.add("org.au");
        twoPartExtensions.add("org.br");
        twoPartExtensions.add("org.cn");
        twoPartExtensions.add("org.do");
        twoPartExtensions.add("org.es");
        twoPartExtensions.add("org.gl");
        twoPartExtensions.add("org.hk");
        twoPartExtensions.add("org.in");
        twoPartExtensions.add("org.kg");
        twoPartExtensions.add("org.ki");
        twoPartExtensions.add("org.lc");
        twoPartExtensions.add("org.mg");
        twoPartExtensions.add("org.mm");
        twoPartExtensions.add("org.ms");
        twoPartExtensions.add("org.nf");
        twoPartExtensions.add("org.ng");
        twoPartExtensions.add("org.ni");
        twoPartExtensions.add("org.nz");
        twoPartExtensions.add("org.pl");
        twoPartExtensions.add("org.ro");
        twoPartExtensions.add("org.ru");
        twoPartExtensions.add("org.sb");
        twoPartExtensions.add("org.sc");
        twoPartExtensions.add("org.sg");
        twoPartExtensions.add("org.tr");
        twoPartExtensions.add("org.tw");
        twoPartExtensions.add("org.uk");
        twoPartExtensions.add("org.ve");
        twoPartExtensions.add("pe.ca");
        twoPartExtensions.add("plc.uk");
        twoPartExtensions.add("police.uk");
        twoPartExtensions.add("ppg.br");
        twoPartExtensions.add("presse.fr");
        twoPartExtensions.add("pro.br");
        twoPartExtensions.add("psc.br");
        twoPartExtensions.add("psi.br");
        twoPartExtensions.add("qc.ca");
        twoPartExtensions.add("qc.com");
        twoPartExtensions.add("qh.cn");
        twoPartExtensions.add("rec.br");
        twoPartExtensions.add("rec.ro");
        twoPartExtensions.add("res.in");
        twoPartExtensions.add("sa.com");
        twoPartExtensions.add("sc.cn");
        twoPartExtensions.add("sch.uk");
        twoPartExtensions.add("se.com");
        twoPartExtensions.add("se.net");
        twoPartExtensions.add("sh.cn");
        twoPartExtensions.add("sk.ca");
        twoPartExtensions.add("slg.br");
        twoPartExtensions.add("sn.cn");
        twoPartExtensions.add("store.ro");
        twoPartExtensions.add("tj.cn");
        twoPartExtensions.add("tm.fr");
        twoPartExtensions.add("tm.mc");
        twoPartExtensions.add("tm.ro");
        twoPartExtensions.add("tmp.br");
        twoPartExtensions.add("tur.br");
        twoPartExtensions.add("tv.br");
        twoPartExtensions.add("tv.tr");
        twoPartExtensions.add("tw.cn");
        twoPartExtensions.add("uk.com");
        twoPartExtensions.add("uk.net");
        twoPartExtensions.add("us.com");
        twoPartExtensions.add("uy.com");
        twoPartExtensions.add("vet.br");
        twoPartExtensions.add("waw.pl");
        twoPartExtensions.add("web.ve");
        twoPartExtensions.add("www.ro");
        twoPartExtensions.add("xj.cn");
        twoPartExtensions.add("xz.cn");
        twoPartExtensions.add("yk.ca");
        twoPartExtensions.add("yn.cn");
        twoPartExtensions.add("zj.cn");
        twoPartExtensions.add("zlg.br");
    }

    /**
     * Returns the main domain of a given domain, respecting the well-known
     * domain extensions with two parts.
     * e.g. for a given domain www.bbc.co.uk it would return bbc.co.uk
     * @param domain a domain, e.g. www.github.com
     * @return the main domain, e.g. github.com
     */
    public static String getMainDomain(String domain) {
        Preconditions.checkNotNull(domain);

        var domainParts = domain.split("\\.");
        var tldLength = getUniqueTldLength(domainParts);

        var mainDomainParts = Arrays.copyOfRange(domainParts, domainParts.length - 1 - tldLength, domainParts.length);
        return Joiner.on(".").join(mainDomainParts);
    }

    private static int getUniqueTldLength(String[] domainParts) {
        var len = domainParts.length;
        if (len <= 1) {
            return 0;
        } else if (len == 2) {
            return 1;
        }

        var tld = Joiner.on(".").join(domainParts[len - 2], domainParts[len - 1]);
        if (twoPartExtensions.contains(tld)) {
            return 2;
        }

        return 1;
    }

    public static String createDaemonRegex(String domain) {
        var regexDomain = domain.replace(".", "\\.");
        return ".*\\." + regexDomain;
    }
}
