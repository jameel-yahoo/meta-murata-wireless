SUMMARY = "Cypress FMAC backport"
DESCRIPTION = "Cypress FMAC backport"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI =  "https://github.com/murata-wireless/cyw-fmac-v4.12-orga/raw/imx-morty-orga/imx-morty-orga_r1.0.tar.gz;name=archive1"
SRC_URI += "https://github.com/murata-wireless/meta-murata-wireless/raw/imx-morty-orga/LICENSE;name=archive99"
          
SRC_URI[archive1.md5sum] = "a3e4e741d7ba3cc129c0163f87cd0dca"
SRC_URI[archive1.sha256sum] = "4f2ce72bac29a214bd59f813bf4a032ce5fa0912ddfbbacc7c7a4e04042c252a"
#LICENSE
SRC_URI[archive99.md5sum] = "b234ee4d69f5fce4486a80fdaf4a4263"
SRC_URI[archive99.sha256sum] = "8177f97513213526df2cf6184d8ff986c675afb514d4e68a404010521b880643"
inherit linux-kernel-base kernel-arch

DEPENDS = " linux-imx"
DEPENDS += " imx-morty-orga"

S = "${WORKDIR}/backporttool-linux-1.0"
B = "${WORKDIR}/backporttool-linux-1.0/"

#You should set variable CROSS_COMPILE, not a CROSS-COMPILE
export CROSS_COMPILE = "${TARGET_PREFIX}"

KERNEL_VERSION = "${@base_read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

do_compile() {
	# Linux kernel build system is expected to do the right thing
	# unset CFLAGS
        echo "TEST_CROSS_COMPILE:: ${CROSS_COMPILE}"
        echo "TEST_CROSSCOMPILE:: ${CROSSCOMPILE}"          
        echo "TEST_TARGET_PREFIX:: ${TARGET_PREFIX}"      
        echo "TEST_ARCH:: ${ARCH}"
        echo "TEST_TARGET_ARCH:: ${TARGET_ARCH}"
        echo "STAGING_KERNEL_BUILDDIR: ${STAGING_KERNEL_BUILDDIR}"
        echo "TEST_LDFLAGS:: ${LDFLAGS}"
        echo "S DIR:  {S}"

        cp -a ${TMPDIR}/work/x86_64-linux/imx-morty-orga/r1.0-r0/imx-morty-orga-r1.0/. .

#       make clean

        oe_runmake KLIB="${STAGING_KERNEL_DIR}" KLIB_BUILD="${STAGING_KERNEL_BUILDDIR}" modules

#        oe_runmake KLIB="${TMPDIR}/work-shared/${MACHINE}/kernel-source" \
#KLIB_BUILD="${TMPDIR}/work/imx6ulevk-poky-linux-gnueabi/linux-imx/4.9.11-r0/build" \
#modules
}

do_install() {
#       install -d ${D}${sbindir}
#       install -m 0644 ${S}/.config ${D}${sbindir}
#	install -m 0644 ${S}/compat/compat.ko ${D}${sbindir}
#	install -m 0644 ${S}/drivers/net/wireless/broadcom/brcm80211/brcmfmac/brcmfmac.ko ${D}${sbindir}
#	install -m 0644 ${S}/drivers/net/wireless/broadcom/brcm80211/brcmutil/brcmutil.ko ${D}${sbindir}
#	install -m 0644 ${S}/net/wireless/cfg80211.ko ${D}${sbindir}


	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/broadcom/brcm80211/brcmfmac
	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/broadcom/brcm80211/brcmutil
	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/compat
	install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/wireless

	install -m 644 ${S}/drivers/net/wireless/broadcom/brcm80211/brcmfmac/brcmfmac.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/broadcom/brcm80211/brcmfmac/brcmfmac.ko
	install -m 644 ${S}/drivers/net/wireless/broadcom/brcm80211/brcmutil/brcmutil.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/broadcom/brcm80211/brcmutil/brcmutil.ko
	install -m 644 ${S}/compat/compat.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/compat/compat.ko
	install -m 644 ${S}/net/wireless/cfg80211.ko ${D}/lib/modules/${KERNEL_VERSION}/kernel/net/wireless/cfg80211.ko
}


PACKAGE_ARCH = "${MACHINE_ARCH}"

#FILES_${PN} += "${sbindir} \
#"


FILES_${PN} += " \
	/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/broadcom/brcm80211/brcmfmac/brcmfmac.ko \	
	/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/broadcom/brcm80211/brcmutil/brcmutil.ko \
	/lib/modules/${KERNEL_VERSION}/kernel/compat/compat.ko \
	/lib/modules/${KERNEL_VERSION}/kernel/net/wireless/cfg80211.ko \
"

PACKAGES += "FILES-${PN}"



