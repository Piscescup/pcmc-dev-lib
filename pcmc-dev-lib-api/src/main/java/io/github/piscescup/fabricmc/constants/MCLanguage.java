package io.github.piscescup.fabricmc.constants;


import java.util.HashMap;
import java.util.Map;

/**
 * This enum class defines all languages that are supported in <b>Minecraft</b>.
 *
 *
 * @author REN YuanTong
 * @since 1.0.0
 * @see <a href="https://minecraft.wiki/w/Language">Language in Minecraft</a>
 */
public enum MCLanguage {
    /**
     * Afrikaans
     */
    AF_ZA("af_za"),

    /**
     * العربية
     */
    AR_SA("ar_sa"),

    /**
     * Asturianu
     */
    AST_ES("ast_es"),

    /**
     * Азербајҹанҹа
     */
    AZ_AZ("az_az"),

    /**
     * Башҡортса
     */
    BA_RU("ba_ru"),

    /**
     * Boarisch
     */
    BAR("bar"),

    /**
     * Беларуская
     */
    BE_BY("be_by"),

    /**
     * Беларуская (латыніцей)
     */
    BE_LATN("be_latn"),

    /**
     * Български
     */
    BG_BG("bg_bg"),

    /**
     * Brezhoneg
     */
    BR_FR("br_fr"),

    /**
     * Braobans
     */
    BRB("brb"),

    /**
     * Bosanski
     */
    BS_BA("bs_ba"),

    /**
     * Català
     */
    CA_ES("ca_es"),

    /**
     * Čeština
     */
    CS_CZ("cs_cz"),

    /**
     * Cymreag
     */
    CY_GB("cy_gb"),

    /**
     * Dansk
     */
    DA_DK("da_dk"),

    /**
     * Österreichisches Deitsch
     */
    DE_AT("de_at"),

    /**
     * Schwiizerdutsch
     */
    DE_CH("de_ch"),

    /**
     * Deutsch
     */
    DE_DE("de_de"),

    /**
     * Ελληνικά
     */
    EL_GR("el_gr"),

    /**
     * English (Australia)
     */
    EN_AU("en_au"),

    /**
     * English (Canada)
     */
    EN_CA("en_ca"),

    /**
     * English (United Kingdom)
     */
    EN_GB("en_gb"),

    /**
     * English (New Zealand)
     */
    EN_NZ("en_nz"),

    /**
     * Pirate Speak
     */
    EN_PT("en_pt"),

    /**
     * ɥsᴉꞁᵷuƎ (uʍoᗡ ǝpᴉsd∩)
     */
    EN_UD("en_ud"),

    /**
     * English (US)
     */
    EN_US("en_us"),

    /**
     * Anglish
     */
    ENP("enp"),

    /**
     * Shakespearean English
     */
    ENWS("enws"),

    /**
     * Esperanto
     */
    EO_UY("eo_uy"),

    /**
     * Español (Argentina)
     */
    ES_AR("es_ar"),

    /**
     * Español (Chile)
     */
    ES_CL("es_cl"),

    /**
     * Español (Ecuador)
     */
    ES_EC("es_ec"),

    /**
     * Español (España)
     */
    ES_ES("es_es"),

    /**
     * Español (México)
     */
    ES_MX("es_mx"),

    /**
     * Español (Uruguay)
     */
    ES_UY("es_uy"),

    /**
     * Español (Venezuela)
     */
    ES_VE("es_ve"),

    /**
     * Andalûh
     */
    ESAN("esan"),

    /**
     * Eesti
     */
    ET_EE("et_ee"),

    /**
     * Euskara
     */
    EU_ES("eu_es"),

    /**
     * فارسی
     */
    FA_IR("fa_ir"),

    /**
     * Suomi
     */
    FI_FI("fi_fi"),

    /**
     * Filipino
     */
    FIL_PH("fil_ph"),

    /**
     * Føroyskt
     */
    FO_FO("fo_fo"),

    /**
     * Français québécois
     */
    FR_CA("fr_ca"),

    /**
     * Français
     */
    FR_FR("fr_fr"),

    /**
     * Frängisch
     */
    FRA_DE("fra_de"),

    /**
     * Furlan
     */
    FUR_IT("fur_it"),

    /**
     * Frysk
     */
    FY_NL("fy_nl"),

    /**
     * Gaeilge
     */
    GA_IE("ga_ie"),

    /**
     * Gàidhlig
     */
    GD_GB("gd_gb"),

    /**
     * Galego
     */
    GL_ES("gl_es"),

    /**
     * ʻŌlelo Hawaiʻi
     */
    HAW_US("haw_us"),

    /**
     * עברית
     */
    HE_IL("he_il"),

    /**
     * हिन्दी
     */
    HI_IN("hi_in"),

    /**
     * Norskt landsmål
     */
    HN_NO("hn_no"),

    /**
     * Hrvatski
     */
    HR_HR("hr_hr"),

    /**
     * Magyar
     */
    HU_HU("hu_hu"),

    /**
     * Հայերեն
     */
    HY_AM("hy_am"),

    /**
     * Bahasa Indonesia
     */
    ID_ID("id_id"),

    /**
     * Igbo
     */
    IG_NG("ig_ng"),

    /**
     * Ido
     */
    IO_EN("io_en"),

    /**
     * Íslenska
     */
    IS_IS("is_is"),

    /**
     * Medžuslovjansky
     */
    ISV("isv"),

    /**
     * Italiano
     */
    IT_IT("it_it"),

    /**
     * 日本語
     */
    JA_JP("ja_jp"),

    /**
     * la .lojban.
     */
    JBO_EN("jbo_en"),

    /**
     * ქართული
     */
    KA_GE("ka_ge"),

    /**
     * Қазақша
     */
    KK_KZ("kk_kz"),

    /**
     * ಕನ್ನಡ
     */
    KN_IN("kn_in"),

    /**
     * 한국어
     */
    KO_KR("ko_kr"),

    /**
     * Kölsch/Ripoarisch
     */
    KSH("ksh"),

    /**
     * Kernewek
     */
    KW_GB("kw_gb"),

    /**
     * Кыргызча
     */
    KY_KG("ky_kg"),

    /**
     * Latina
     */
    LA_LA("la_la"),

    /**
     * Lëtzebuergesch
     */
    LB_LU("lb_lu"),

    /**
     * Limburgs
     */
    LI_LI("li_li"),

    /**
     * Lombard
     */
    LMO("lmo"),

    /**
     * ລາວ (ປະເທດລາວ)
     */
    LO_LA("lo_la"),

    /**
     * LOLCAT
     */
    LOL_US("lol_us"),

    /**
     * Lietuvių
     */
    LT_LT("lt_lt"),

    /**
     * Latviešu
     */
    LV_LV("lv_lv"),

    /**
     * 文言文
     */
    LZH("lzh"),

    /**
     * Македонски
     */
    MK_MK("mk_mk"),

    /**
     * Монгол
     */
    MN_MN("mn_mn"),

    /**
     * Bahasa Melayu
     */
    MS_MY("ms_my"),

    /**
     * Malti
     */
    MT_MT("mt_mt"),

    /**
     * Mēxikatlahtōlli
     */
    NAH("nah"),

    /**
     * Platdüütsk
     */
    NDS_DE("nds_de"),

    /**
     * Vlaams
     */
    NL_BE("nl_be"),

    /**
     * Nederlands
     */
    NL_NL("nl_nl"),

    /**
     * Norsk nynorsk
     */
    NN_NO("nn_no"),

    /**
     * Norsk Bokmål
     */
    NOB_NO("no_no"),

    /**
     * Occitan
     */
    OC_FR("oc_fr"),

    /**
     * Övdalsk
     */
    OVD("ovd"),

    /**
     * Polski
     */
    PL_PL("pl_pl"),

    /**
     * Ngiiwa
     */
    PLS("pls"),

    /**
     * Português (Brasil)
     */
    PT_BR("pt_br"),

    /**
     * Português (Portugal)
     */
    PT_PT("pt_pt"),

    /**
     * Quenya
     */
    QYA_AA("qya_aa"),

    /**
     * Română
     */
    RO_RO("ro_ro"),

    /**
     * Дореформенный русскій
     */
    RPR("rpr"),

    /**
     * Русский
     */
    RU_RU("ru_ru"),

    /**
     * Руснацькый
     */
    RY_UA("ry_ua"),

    /**
     * Сахалыы (Cаха Сирэ)
     */
    SAH_SAH("sah_sah"),

    /**
     * Davvisámegiella
     */
    SE_NO("se_no"),

    /**
     * Slovenčina
     */
    SK_SK("sk_sk"),

    /**
     * Slovenščina
     */
    SL_SI("sl_si"),

    /**
     * Af-Soomaali
     */
    SO_SO("so_so"),

    /**
     * Shqip
     */
    SQ_AL("sq_al"),

    /**
     * Srpski
     */
    SR_CS("sr_cs"),

    /**
     * Српски
     */
    SR_SP("sr_sp"),

    /**
     * Svenska
     */
    SV_SE("sv_se"),

    /**
     * Säggs'sch
     */
    SXU("sxu"),

    /**
     * Ślōnskŏ
     */
    SZL("szl"),

    /**
     * தமிழ்
     */
    TA_IN("ta_in"),

    /**
     * ภาษาไทย
     */
    TH_TH("th_th"),

    /**
     * Tagalog
     */
    TL_PH("tl_ph"),

    /**
     * tlhIngan Hol
     */
    TLH_AA("tlh_aa"),

    /**
     * toki pona
     */
    TOK("tok"),

    /**
     * Türkçe
     */
    TR_TR("tr_tr"),

    /**
     * Татарча
     */
    TT_RU("tt_ru"),

    /**
     * Bats'i k'op
     */
    TZO_MX("tzo_mx"),

    /**
     * Українська
     */
    UK_UA("uk_ua"),

    /**
     * Català (Valencià)
     */
    VAL_ES("val_es"),

    /**
     * Vèneto
     */
    VEC_IT("vec_it"),

    /**
     * Tiếng Việt
     */
    VI_VN("vi_vn"),

    /**
     * Viossa
     */
    VP_VL("vp_vl"),

    /**
     * ייִדיש
     */
    YI_DE("yi_de"),

    /**
     * Yorùbá
     */
    YO_NG("yo_ng"),

    /**
     * 简体中文
     */
    ZH_CN("zh_cn"),

    /**
     * 繁體中文（香港）
     */
    ZH_HK("zh_hk"),

    /**
     * 繁體中文（台灣）
     */
    ZH_TW("zh_tw"),

    /**
     * بهاس ملايو
     */
    ZLM_ARAB("zlm_arab");

    private static final Map<String, MCLanguage> CACHE = new HashMap<>();

    private final String code;

    public static final MCLanguage[] LANGUAGES = MCLanguage.values();

    static {
        for (MCLanguage language : LANGUAGES) {
            CACHE.put(language.code, language);
        }
    }

    MCLanguage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MCLanguage fromLangCode(String code) {
        return CACHE.getOrDefault(code, EN_US);
    }

}